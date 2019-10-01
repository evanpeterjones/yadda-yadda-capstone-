(ns db-connection
  (:require [yesql.core :refer [defqueries]]
            [jdbc.pool.c3p0 :as pool]
            [clojure.java.jdbc :as jdbc]))

(def db-uri
  (java.net.URI. (or
                 (System/getenv "DATABASE_URL")
                 "postgres://kvzhgjwupmfymm:e19fdd9e40d5783820d13ab6fe141ebb00c8a60b47efd63337180ea068b3a4ef@ec2-107-20-173-2.compute-1.amazonaws.com:5432/d2na7ais8vs462")))

(def user-and-password
  (if (nil? (.getUserInfo db-uri))
    nil (clojure.string/split (.getUserInfo db-uri) #":")))

(def db-spec "postgresql://localhost:5432/ducts")

;; TODO: fix connection to the postgresql db on heroku instead of my local db
(comment (pool/make-datasource-spec
   {:classname "org.postgresql.Driver"
    :subprotocol "postgresql"
    :user (get user-and-password 0)
    :password (get user-and-password 1)
    :subname (if (= -1 (.getPort db-uri))
               (format "//%s%s" (.getHost db-uri) (.getPort db-uri))
               (format "//%s:%s%s" (.getHost db-uri) (.getPort db-uri) (.getPath db-uri)))}))

(defn migrated? []
  "Query to check if db is up"
  (-> (jdbc/query db-spec
                  [(str "select count(*) from information_schema.tables "
                        "where table_name='ducts'")])
      first :count pos?))

(defn migrate []
  (when (not (migrated?))
    (print "Creating Database Structure...") (flush)
    (jdbc/db-do-commands db-spec
                         (jdbc/create-table-ddl
                           :ducts
                          [:id :serial "PRIMARY KEY"]
                          [:body :varchar "NOT NULL"]
                          [:ducts :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]
                          (println " done")))))

(defn tick []
  (jdbc/insert! db-spec :ducts [:body] ["hello"]))

;(declare create-user!)

(defqueries "procedures.sql"
  {:connection db-spec})

(defn query [q]
  (jdbc/query db-spec [q]))
 
(defn top-posts
    ([] (top-posts 28607))
    ([location] (jdbc/query db-spec [(str "SELECT * FROM POSTS
                                           WHERE PST_LOC_FK = '" location
                                          "' ORDER BY PST_Time DESC")])))
