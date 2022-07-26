(ns core
  (:require [clojure.java.io :as io])
  (:import [java.io FileNotFoundException])
  (:use [http]
        [script-scraper]
        [files])
  (:gen-class))

(def ^:dynamic collected-results {})

(def http "https://")
(def base ".craigslist.org/search/sss?query=")
(def cities ["oklahomacity" "boone"])

(def query)

(defn query-url [city query]
  (str http city base (clojure.string/replace query #" " "+") "&sort=rel"))

(defn item [res n] (-> (nth res n) :title :content first))

(defn titles [test] (for [n (range (count test))] (item test n)))

(defn select [result-set query]
  result-set)

(defn search-city [city query]
  (let [url (query-url city query)
        items (script-scraper/query-items url)]
    (for [item items]
      (do
        (clojure.pprint/pprint (str "Found item: " item))
        (select item query)))))

(defn -main []
  (for [city cities]
    (do
      (clojure.pprint/pprint (str "Processing City Query: " city))
      (files/make-dir city)
      (search-city city query))))


;;; OLD CODE DELETE LATER, KEEP FOR REFERENCE
(defn title-to-url 
  [base-url & args]
  (str base-url (reduce #(str %1 (clojure.string/replace %2 #" " "-")) args)))

(defn process-movie [movie-name genre]
  (clojure.pprint/pprint (str "    Processing Script: " movie-name))
  (try
    (if (files/script-not-exists? movie-name)
      (->> (title-to-url base scripts (files/parse-movie-name movie-name) ".html")
           (script-scraper/parse-script-from-movie-page)
           (spit (files/make-file genre movie-name))))
    (catch FileNotFoundException e
      (clojure.pprint/pprint (str "File Not Found for Script : " movie-name)))))

(defn process-genre [genre-name]
  (let [genre-page-url (title-to-url base genre genre-name)
        movies (script-scraper/get-movies-from-genre-page genre-page-url)]
    (for [movie-title movies]
      (do
        (clojure.pprint/pprint (str "  Processing Movie: " movie-title))
        (process-movie movie-title genre-name)))))


