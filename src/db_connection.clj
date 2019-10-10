(ns db-connection
  (:require [yesql.core :refer [defqueries]]
;;            [jdbc.pool.c3p0 :as pool] ;; this is unecessary right now for the prototype
            [clj-time.core :as t]
            [clojure.java.jdbc :as jdbc]))

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
  (jdbc/query db-spec [q]))

;; generate yesql procedures
(defqueries "upgrade.sql"
  {:connection db-spec})

(defqueries "procedures.sql"
  {:connection db-spec})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; SESSION QUERIES ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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
    (jdbc/insert! db-spec :sessions {:ses_id ses-id, :ses_createdon (t/now)})
    ses-id))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; LOCATION QUERIES ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-location [zip-code]
 (query (str "SELECT * FROM LOCATION WHERE AREA_ID_PK = '" zip-code "';")))
      
