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

(declare get-posts get-location-from-session)

(defn upgrade-version [newest-version]
  "update version table to reflect most recently run database scripts"
  (jdbc/execute! db-spec [(str "UPDATE VERSION SET CURR = " newest-version ";")]))

(defn upgrade [current-version]
  (let [upgrade-fns (hugsql.core/map-of-db-fns "upgrade.sql")
        newest-version (apply max (map #(Integer. (name %)) (keys upgrade-fns)))]
    (if (> newest-version current-version)
      (->> (keys upgrade-fns)
           (filter #(< current-version (-> % name Integer.)))
           (apply #((-> % 
                        upgrade-fns
                        :fn) db-spec))))
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
  (query ["SELECT USR_ID_PK "
          "FROM SESSIONS, USERS"
          "WHERE SES_ID = '" session-id "';"]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; SESSION QUERIES ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Queries ;;

(defn session-exists? [ses-id]
  (let [results (query (str "SELECT 1 FROM SESSIONS WHERE SES_ID = '" ses-id "';"))]
    (not-empty results)))

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

(defn get-location-from-session [session]
  "given a sessionid find the associated location id"
  (-> (query (str "SELECT LOC_ID_PK "
                  "FROM SESSIONS, LOCATION "
                  "WHERE SES_ID = '"
                  session "'"))
      first
      :loc_id_pk))

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

(defn associate-session-and-zip [loc-data session-id]
  (let [zip (loc/get-location-value loc-data :zip)
        zip-id (if (location-exists? zip)
                 (:loc_id_pk (get-location-id zip))
                 nil)]
    (if (and (location-exists? zip) (session-exists? session-id))
      ;; update session table to reference location
      (jdbc/update! db-spec
                    :sessions
                    {:ses_loc_fk zip-id}
                    ["ses_id=?" session-id])
      (do
        ;; TODO: this assumes session exists, there might be an edge case where it does not
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

(defn create-new-post [user content location-fk]
  (jdbc/execute! db-spec 
                 [(str "INSERT INTO POSTS (pst_usr_id_fk, pst_content, pst_loc_fk, pst_time)"
                       "VALUES(?,?,?,now())") user content location-fk]))