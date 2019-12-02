(ns ducts.utils.database-util
  (:use [clojure.data.json :as json]))

(defn get-json-row [obj]
  "json_agg function in psql is applied to separate rows -_-"
  (-> obj
      :json_agg
      .getValue))

(defn map-string [strng]
  (json/write-str strng))

(defn json-string-to-map [o-str]
  (json/read-str o-str))

(defn construct-json [result]
  "this is specific to cases when json_agg function is used on query"
  (json/write-str (map #(get % 0) 
                       (map json/read-str (map get-json-row result)))))
