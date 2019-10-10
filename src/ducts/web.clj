(ns ducts.web
  (:use compojure.core
        ring.middleware.json
        ring.util.response
        [ring.adapter.jetty :as jetty])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ducts.views :as views]
            [ducts.cookie-util :as cku]
            [db-connection :as dbc])
  (:gen-class))

(declare req-obj)

(defroutes app
  (GET "/" request
       (def req-obj request)
       {:status 200
        :headers {"Content-Type" "text/html"}
        :cookies {"yapp-session" {:value (let [ses-id (cku/get-cookie-from-request request)]
                                           (if (dbc/session-exists? ses-id)
                                             ses-id
                                             (dbc/create-session)))
                                  :max-age (* 60 24 30 365)}} ;; cookie should last for 1 year (?)
        :body (views/web-app)})
  (GET "/db" []
       {:status 200
        :headers {"Content-Type" "text/html"}
        :body (dbc/query "select * from posts")})
  (GET "/feed" []
       {:status 200
        :headers {"Content-Type" "text/html"}
        :body "asdf"})
                  ;; TODO: /feed overloading with pagination
  (route/resources "/")
  (route/not-found (views/not-found)))

(defn -main [& [port]]
  (let [port (Integer. (or (System/getenv "PORT") port 5000))]
    (jetty/run-jetty (handler/site #'app) {:port port :join? false})))
