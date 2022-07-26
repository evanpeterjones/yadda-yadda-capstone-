(ns ducts.utils.database-util
  (:use [clojure.data.json :as json]))

(defn get-json-row [obj]
  "json_agg function in psql is applied to separate rows -_-"
  (-> obj
      :json_agg))

(defn map-string [strng]
  (json/write-str strng))

(defn json-string-to-map [o-str]
  (json/read-str o-str))

(defn construct-json [result]
  "this is specific to cases when json_agg function is used on query"
  (str "[" (apply str (map (fn [row] (-> row
                                         get-json-row
                                         .getValue
                                         json/read-str
                                         first
                                         json/write-str)) result)) "]"))
