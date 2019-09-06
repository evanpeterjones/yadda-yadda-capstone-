(ns ducts.web
  (:use compojure.core
        ring.middleware.json
        ring.util.response
        [ring.adapter.jetty :as jetty])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ducts.views :as views]
            [db-connection :as dbc]
            [clojure.java.jdbc :as jdbc])
  (:gen-class))

(defroutes app
  ;; TODO: default route loads webpage
  (GET "/" []
       {:status 200
        :headers {"Content-Type" "text/html"}
        :cookies {"SessionId" {:value "12345"
                               :max-age (* 60 24 30)}}
        :body (views/web-app)})
  ;; TODO: ensure api route actually connects to db
  (GET "/createuser" [username email] (dbc/create-user! {:username username :email email}))
  (GET "/test" [] (dbc/query))
  (GET "/api" [which]
       (response {:email "evanpeterjones@gmail.com"
                  :result which}))
  (comment (GET "/api" [email which]
       (response {:email email
                  :result which})))

  (GET "/db" []
       (dbc/tick)
       {:status 200
        :headers {"Content-Type" "text/plain"}
        :body (str "Ticks: " (first (jdbc/query ["select count(*) from ducts"])))})

  ;; TODO: write api routes that get data
  ;; TODO: write api routes for posting data
  ;;(POST "/api" [q] )
  (route/resources "/"))

(def application (wrap-json-response app))

(defn -main [& [port]]
  ;;(dbc/migrate)
  (let [port (Integer. (or (System/getenv "PORT") port 5000))]
    (jetty/run-jetty (handler/site #'application) {:port port :join? false})))

                                        ;(.stop server)
                                        ;(def server (-main))


