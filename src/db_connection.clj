(ns db-connection
  (:use [clojure.pprint])
  (:require [hugsql.core :as hugsql]
            [clojure.java.jdbc :as jdbc]
            [clojure.spec.alpha :as s]
            [ducts.utils.location :as loc]
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

(declare get-posts get-post get-location-from-session create-post<!)

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
           (apply #((-> % upgrade-fns :fn) db-spec))))
    (upgrade-version newest-version)))

(hugsql/def-db-fns "procedures.sql")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; MISCELLANEOUS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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

(defn get-user-from-session-id [session-id]
  (-> (query (str "select USR_ID_PK "
                  "FROM SESSIONS, USERS "
                  "WHERE SES_ID = '" session-id "' "
                  "AND SES_USR_ID_FK = USR_ID_PK"))
      first
      :usr_id_pk))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; SESSION QUERIES ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Queries ;;

(defn session-exists? [ses-id]
  (let [results (query (str "SELECT 1 FROM SESSIONS WHERE SES_ID = '" ses-id "';"))]
    (not-empty results)))

(defn get-user-by-session [session]
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

(defn get-location-from-session [session]
  "given a sessionid find the associated location id"
  (-> (query (str "SELECT SES_LOC_FK "
                  "FROM SESSIONS, LOCATION "
                  "WHERE SES_ID = '" session "'"))
      first
      :ses_loc_fk))

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
  (let [zip-id (if (location-exists? zip)
                 (:loc_id_pk (first (get-location-id zip)))
                 nil)
        user-id (get-user-by-session session-id)]
    (if (and zip-id (location-exists? zip) (session-exists? session-id))
      (jdbc/update! db-spec
                    :sessions
                    {:ses_loc_fk zip-id
                     :ses_usr_id_fk session-id}
                    ["ses_id=?" session-id]))))

(defn associate-session-and-location-data [loc-data session-id]
  (let [zip (loc/get-location-value loc-data :zip)
        zip-id (if (location-exists? zip)
                 (:loc_id_pk (first (get-location-id zip)))
                 nil)
        user-id (get-user-by-session session-id)]
    (if (and (location-exists? zip) (session-exists? session-id))
      ;; update session table to reference location
      (jdbc/update! db-spec
                    :sessions
                    {:ses_loc_fk zip-id
                     :ses_usr_id_fk session-id}
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

(defn get-post-by-id [post-id]
  "wrapper for hugsql query"
  (-> (get-post db-spec {:post_id post-id})
      first
      :json_agg
      (.getValue)))

(defn create-new-post [user content location-fk]
  "wrapper for hugsql query"
  (-> (create-post<! db-spec {:user user :content content :location location-fk})
      first
      :pst_id_pk))

(defn post-exists? [pid]
  (not (.isEmpty (query (str "SELECT * FROM POSTS WHERE PST_ID_PK = '" pid "';")))))

(defn delete-post [pid]
  (jdbc/execute! db-spec
                 [(str "DELETE FROM POSTS WHERE PST_ID_PK = '" pid "';")]))

(defn get-user-id-from-post-id [pid]
  (if (post-exists? pid)
    (-> (query (str "SELECT PST_USR_ID_FK "
                    "FROM POSTS "
                    "WHERE PST_ID_PK = '" pid "';"))
        first
        :pst_usr_id_fk)
    nil))
