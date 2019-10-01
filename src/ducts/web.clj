(ns ducts.web
  (:use compojure.core
        ring.middleware.json
        ring.util.response
        [ring.adapter.jetty :as jetty])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ducts.views :as views]
            [db-connection :as dbc])
  (:gen-class))

(defroutes app
  (GET "/" []
       {:status 200
        :headers {"Content-Type" "text/html"}
        :cookies {"yadda-session" {:value "12345" ;; (dbc/
                                   :max-age (* 60 24 30)}}
        :body (views/web-app)})
  (GET "/db" []
       {:status 200
        :headers {"Content-Type" "text/html"}
        :body (dbc/query "select * from posts")})
  (GET "/post:id" []
       {:status 200
        :headers {"Content-Type" "text/html"}
        :body (dbc/query "select * from posts")})
  (route/resources "/")
  (route/not-found (views/not-found)))

(defn -main [& [port]]
  (let [port (Integer. (or (System/getenv "PORT") port 5000))]
    (jetty/run-jetty (handler/site #'app) {:port port :join? false})))
