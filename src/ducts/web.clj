(ns ducts.web
  (:use compojure.core
        ring.middleware.json
        ring.util.response
        [ring.adapter.jetty :as jetty])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ducts.views :as views]
            [ducts.api :as api]
            [db-connection :as dbc]
            [clojure.java.jdbc :as jdbc])
  (:gen-class))

(declare server)

(defroutes app
  (GET "/" []
       {:status 200
        :headers {"Content-Type" "text/html"}
        :cookies {"yadda-session" {:value "12345" ;; (dbc/generate-cookie)
                                   :max-age (* 60 24 30)}}
        :body (views/web-app)})
  (route/resources "/")
  (route/not-found (views/not-found)))
  ;(api/api-routes))

;(def app api/api-routes)
;(def application (wrap-json-response app))

(defn -main [& [port]]
  ;;(dbc/migrate)
  (let [port (Integer. (or (System/getenv "PORT") port 5000))]
    (jetty/run-jetty (handler/site #'app) {:port port :join? false})))
