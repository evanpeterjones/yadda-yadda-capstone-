(ns ducts.utils.article-parser
  (:require [clojure.data.json :as json]
            [clj-http.client :as client])
  (:use [readie.core])
  (:gen-class))

(defn parse-page [page-data]
  page-data)

(defn pull-and-parse
  [url]
  (-> url
      (client/get {:accept :json})
      :body
      parse-page))


