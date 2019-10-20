(ns db-connection
  (:require [yesql.core :refer [defqueries]]
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

(defn migrated? []
  "Query to check if db is up" ;; this needs to be updated because it's not accurate
  (-> (jdbc/query db-spec
                  [(str "select count(*) from information_schema.tables "
                        "where table_name='ducts'")])
      first :count pos?))

(defn query [q]
  "A function for testing sql queries"
  (jdbc/query db-spec [q]))

;; generate yesql procedures
(defqueries "upgrade.sql"
  {:connection db-spec})

(defqueries "procedures.sql"
  {:connection db-spec})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; SESSION QUERIES ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; QUERIES ;;

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
                   :ses_createdon (new Timestamp (.getMillis (t/now)))})
    ses-id))

(defn clear-used-sessions [])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; LOCATION QUERIES ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;(s/def 

(defn location-exists? [zip]
  (not-empty (query (str "SELECT 1 FROM Location WHERE LOC_ID_PK = '" zip "';"))))

(defn create-location 
  "create a new location in db"
  ([zip city state]
   (jdbc/insert! db-spec :location
                 {:loc_id_pk zip
                  :loc_alias city
                  :loc_state state}))
  ([loc-data]
   (create-location
    (loc/get-location-value loc-data :zip)
    (loc/get-location-value loc-data :city)
    (loc/get-location-value loc-data :state))))

(defn associate-session-and-zip [loc-data session-id]
  (let [zip (loc/get-location-value loc-data :zip)]
    (if (and (location-exists? zip) (session-exists? session-id))
      (jdbc/update! db-spec
                    :sessions
                    {:ses_loc_fk zip}
                    ["ses_id=?" session-id])
      (do
        (create-location loc-data)
        (associate-session-and-zip loc-data session-id)))))

(defn get-location-alias 
  "get location alias from zip"
  ([] (get-location-alias "28607"))
  ([zip] (query (str "SELECT LOC_ALIAS FROM LOCATION WHERE LOC_ID_PK = '" zip "';"))))
