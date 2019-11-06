(ns ducts.web
  (:use compojure.core
        ring.middleware.json
        ring.util.response
        [ring.adapter.jetty :as jetty])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ducts.views :as views]
            [ducts.utils.cookies :as cku]
            [ducts.utils.location :as loc]
            [ducts.utils.database-util :as dbu]
            [db-connection :as dbc])
  (:gen-class))

(declare ro)

(defroutes app
  (GET "/" request
    (def ro request)
    {:status 200
     :headers {"Content-Type" "text/html"}
     :cookies {"yapp-session" {:value (let [ses-id (cku/get-cookie-from-request request)]
                                        (if (dbc/session-exists? ses-id)
                                          ses-id
                                          (dbc/create-session)))
                               :max-age (* 60 24 30 365)}}
     :body (views/web-app)})
  
  (GET "/feed" request
    ;; this still needs pagination so we don't just ship out all of our posts (long term)
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (->> (cku/get-cookie-from-request request)
                (dbc/get-location-from-session)
                (hash-map :location)
                (dbc/get-posts dbc/db-spec)
                dbu/construct-json)})
  
  (GET "/bounce" request
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body "adsf"})

  (GET "/getzip" [lat long :as req]
    "this route returns a zipcode when given lat and longitude"
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (let [ses (cku/get-cookie-from-request req)]
             (if ses
               (str (dbc/get-zip-from-session ses))
               (let [data (loc/get-location-data lat long)]
                 (dbc/associate-session-and-location-data data ses)
                 (loc/get-location-value data :zip))))})

  (GET "/getUserFromSession" request
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (str (->> (cku/get-cookie-from-request request)
                        dbc/get-user-from-session-id))})

  (GET "/newPost" [content :as request]
       (def ro request)
        {:status 200
        :headers {"Content-Type" "application/json"}
        :body (let [ses (cku/get-cookie-from-request request)
                    user (dbc/get-user-from-session-id ses)
                    location-fk (dbc/get-location-from-session ses)]
                (if (not (-> content .trim .isEmpty))
                  (-> (dbc/create-new-post user content location-fk)
                      dbc/get-post-by-id)))})

  (GET "/setUserLocation" [zip :as request]
       (def ro request)
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (let [ses (cku/get-cookie-from-request request)]
                 (if (and ses (dbc/location-exists? zip))
                   (if (dbc/associate-session-and-zip zip ses)
                     zip
                     nil)
                   (let [loc-data (loc/get-location-data zip)]
                     (dbc/associate-session-and-location-data loc-data ses)
                     (loc/get-location-value loc-data :zip))))})

  (POST "/deletePost" request
        (def ro request)
        (let [user-id (->> (cku/get-cookie-from-request request)
                           dbc/get-user-from-session-id)
              post-id (-> request :query-string (.split "=") (get 1))]
          (if (= user-id (dbc/get-user-id-from-post-id post-id))
            (do (dbc/delete-post post-id)
                (str "deleted post: " post-id))
            "post not deleted")))

  (route/resources "/")
  (route/not-found (views/not-found)))

(defn -main [& [port]]
  (dbc/checkup)
  (let [port (Integer. (or (System/getenv "PORT") port 5000))]
    (jetty/run-jetty (handler/site #'app) {:port port :join? false})))
