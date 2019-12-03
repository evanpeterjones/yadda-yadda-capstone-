(ns db-connection
  (:use [clojure.pprint])
  (:require [hugsql.core :as hugsql]
            [clojure.java.jdbc :as jdbc]
            [clojure.spec.alpha :as s]
            [ducts.utils.location :as loc]
            [ducts.utils.database-util :as dbu]
            [clj-time.core :as t])
  (:import (java.sql Timestamp)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; GENERAL SETUP ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def db-spec (or (System/getenv "DATABASE_URL")
                 {:dbtype "postgresql"
                  :dbname "evanpeterjones"
                  :subprotocol "postgresql"
                  :subname "//localhost:5432/evanpeterjones"
                  :host "localhost"
                  :port "5432"
                  :user "evanpeterjones"
                  :password "Avogadro6.02"}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; HUG SQL QUERIES ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(declare get-post-and-comments
         get-posts-by-alias
         get-user-information
         get-my-posts
         get-posts
         get-post
         get-link
         create-post<!
         create-reply-post<!)

(defn upgrade-version [newest-version]
  "update version table to reflect most recently run database scripts"
  (jdbc/execute! db-spec [(str "UPDATE VERSION SET CURR = " newest-version ";")]))

(defn upgrade [current-version]
  (let [upgrade-fns (hugsql.core/map-of-db-fns "upgrade.sql")
        newest-version (apply max (map #(Integer. (name %)) (keys upgrade-fns)))]
    (if (> newest-version current-version)
      ;; "apply the database upgrades where the current version is <
      (->> (keys upgrade-fns)
           (filter #(< current-version (-> % name Integer.)))
           (map #((-> % upgrade-fns :fn) db-spec))))
    (upgrade-version newest-version)))

(hugsql/def-db-fns "procedures.sql")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; MISCELLANEOUS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn long-link [short-link]
  (let [url (get-link db-spec {:short short-link})]
    (if url
      (-> url
          first
          :json_agg
          .getValue)
      nil)))

(defn query [q]
  "A function for testing sql queries"
  (jdbc/query db-spec [q]))

(defn get-db-version 
  []
  (-> (query "SELECT CURR FROM VERSION;")
      first
      :curr))

(defn checkup []
  "check if db needs to be upgraded and perform upgrade"
  (let [version (get-db-version)]
    (pprint (str "Database v." version))
    (upgrade version)
    (pprint (str "Upgraded v." (get-db-version)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; USER QUERIES ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn update-user-info [user-id username email]
  (jdbc/update! db-spec
                :users
                {:usr_username username
                 :usr_email email}
                ["usr_id_pk=?" (Integer. user-id)]))

(defn get-user-from-session-id [session-id]
  (-> (query (str "select USR_ID_PK "
                  "FROM SESSIONS, USERS "
                  "WHERE SES_ID = '" session-id "' "
                  "AND SES_USR_ID_FK = USR_ID_PK"))
      first
      :usr_id_pk))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; SESSION QUERIES ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Queries ;;

(defn session-sync [session]
  )

(defn session-exists? [ses-id]
  (let [results (query (str "SELECT 1 FROM SESSIONS WHERE SES_ID = '" ses-id "';"))]
    (not-empty results)))

(defn session-user-exists? [session]
  "returns nil or returns the user id"
  (-> (query (str "SELECT SES_USR_ID_FK "
                  "FROM SESSIONS "
                  "WHERE ses_id = '" session "';"))
      first
      :ses_usr_id_fk))

(defn get-session-id []
  (let [id (:md5 (first (query "SELECT MD5(RANDOM()::text);")))]
    (if (session-exists? id)
      (get-session-id)
      id)))

(defn create-session []
  "generates a session ID for client and inserts into DB"
  (let [ses-id (get-session-id)]
    (jdbc/insert! db-spec :sessions
                  {:ses_id ses-id
                   :ses_usr_id_fk (-> (create-user db-spec) first :usr_id_pk)
                   :ses_createdon (new Timestamp (.getMillis (t/now)))})
    ses-id))

(defn clear-used-sessions [])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; LOCATION QUERIES ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn location-exists? [zip]
  (not-empty (query (str "SELECT 1 FROM Location WHERE LOC_ID = '" zip "';"))))

(defn get-zip-from-session [ses]
  "given a sessionid find the associated location id"
  (-> (query (str "SELECT LOC_ID "
                  "FROM SESSIONS, LOCATION "
                  "WHERE SES_ID = '"
                  ses "'"))
      first
      :loc_id))

(defn get-location-data-from-session [session]
  "given a sessonID return all data"
  (if session
    (-> (query (str "SELECT json_agg(location) "
                    "FROM SESSIONS "
                    "LEFT JOIN LOCATION "
                    "ON loc_id_pk = ses_loc_fk "
                    "WHERE SES_ID = '" session "'"))
        first 
        :json_agg
        .getValue)))

(defn get-location-from-session [session]
  "given a sessionID find the associated location id"
  (if session 
    (-> (query (str "SELECT SES_LOC_FK "
                    "FROM SESSIONS, LOCATION "
                    "WHERE SES_ID = '" session "'"))
        first
        :ses_loc_fk)
    nil))

(defn create-location 
  "create a new location in db"
  ([zip city state]
   (jdbc/insert! db-spec :location
                 {:loc_id zip
                  :loc_alias city
                  :loc_state state}))
  ([loc-data]
   (create-location
    (loc/get-location-value loc-data :zip)
    (loc/get-location-value loc-data :city)
    (loc/get-location-value loc-data :state))))

(defn get-location-id [zip]
  (query (str "SELECT LOC_ID_PK FROM Location "
              "WHERE LOC_ID = '" zip "'")))

(defn get-location-alias 
  "get location alias from zip"
  ([] (get-location-alias "28607"))
  ([zip] (query (str "SELECT LOC_ALIAS FROM LOCATION WHERE LOC_ID = '" zip "';"))))

(defn associate-session-and-zip [zip session-id]
  (pprint (str zip " " session-id))
  (let [zip-id (if (location-exists? zip)
                 (:loc_id_pk (first (get-location-id zip)))
                 nil)
        user-id (session-user-exists? session-id)]
    (if (and zip-id (location-exists? zip) (session-exists? session-id))
      (jdbc/update! db-spec
                    :sessions
                    {:ses_loc_fk zip-id
                     :ses_usr_id_fk user-id}
                    ["ses_id=?" session-id]))))

(defn associate-session-and-location-data [loc-data session-id]
  (let [zip (loc/get-location-value loc-data :zip)
        zip-id (if (location-exists? zip)
                 (:loc_id_pk (first (get-location-id zip)))
                 nil)
        user-id (get-user-from-session-id session-id)]
    (if (location-exists? zip)
      (jdbc/update! db-spec
                    :sessions
                    {:ses_loc_fk zip-id
                     :ses_usr_id_fk user-id}
                    ["ses_id=?" session-id])
      (do
        ;; TODO: assumes session exists, might be an edge case where it does not?
        (create-location loc-data)
        (associate-session-and-zip loc-data session-id)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; POST QUERIES ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn recent-posts []
  "get most recent posts"
  (let [res (query (str "SELECT * FROM POSTS;"))]
    (if res
      (do (println res)
          res)
      nil)))

(defn rm-parent-comment [id]
  "idk why I ever set this up like this, should've been a fk instead or something"
  (jdbc/execute! db-spec
                 [(str "UPDATE POSTS SET pst_hascomments = false WHERE PST_ID_PK = '" id "';")]))

(defn get-post-by-id [post-id]
  "wrapper for hugsql query"
  (-> (get-post db-spec {:post_id post-id})
      first
      :json_agg
      .getValue))

(defn create-new-post
  ([u c lf] (create-new-post u c lf nil))
  ([user content location-fk parent]
   "wrapper for hugsql query"
   (if parent
     (jdbc/execute! db-spec
                    [(str "UPDATE POSTS SET pst_hascomments = true WHERE PST_ID_PK = '" parent "';")]))
   (-> (create-post<! db-spec {:user user :content content :location location-fk :parent parent})
       first
       :pst_id_pk)))

(defn post-exists? [pid]
  (not (.isEmpty (query (str "SELECT * FROM POSTS WHERE PST_ID_PK = '" pid "';")))))

(defn delete-post [pid]
  (jdbc/execute! db-spec
                 [(str "DELETE FROM POSTS WHERE PST_PARENT_FK = '" pid "' "
                       "AND PST_ID_PK = '" pid "';")]))

(defn get-user-id-from-post-id [pid]
  (if (post-exists? pid)
    (-> (query (str "SELECT PST_USR_ID_FK "
                    "FROM POSTS "
                    "WHERE PST_ID_PK = '" pid "';"))
        first
        :pst_usr_id_fk)
    nil))
