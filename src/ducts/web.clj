(ns ducts.web
  (:use compojure.core
        ring.middleware.json
        ring.util.response
        [ring.adapter.jetty :as jetty])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.util.response :as r]
            [ducts.views :as views]
            [ducts.utils.cookies :as cku]
            [ducts.utils.location :as loc]
            [ducts.utils.database-util :as dbu]
            [db-connection :as dbc])
  (:gen-class))

(declare ro)

(defroutes app-routes
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

  (GET "/feed" [offset :as request]
       (def ro request)
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (let [loc_data (->> request
                                  cku/get-cookie-from-request
                                  dbc/get-location-data-from-session
                                  dbu/json-string-to-map
                                  first)]
                (->
                 (dbc/get-posts dbc/db-spec
                                {:location (get loc_data "loc_id_pk")
                                 :lim 5
                                 :offset (Integer/parseInt offset)})
                 dbu/construct-json))})

  (GET "/myPosts" request
       (def ro request)
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (let [user-id (->> request
                                 cku/get-cookie-from-request
                                 dbc/get-user-from-session-id)]
                (dbu/construct-json
                 (dbc/get-my-posts dbc/db-spec {:me user-id})))})

  (GET "/postComments" [post]
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (-> (dbc/get-post-and-comments dbc/db-spec
                                             {:pst_id (Integer/parseInt post)})
                  dbu/construct-json)})

  (GET "/bounce" request
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body "adsf"})

  (GET "/userInformation" request
       (def ro request)
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (let [user-id (->> request
                                 cku/get-cookie-from-request
                                 dbc/get-user-from-session-id)]
                (if user-id
                  (->
                   (dbc/get-user-information dbc/db-spec {:id (Integer. user-id)})
                   first
                   :json_agg
                   .getValue)
                  ""))})

  (GET "/setUserLocation" [zip :as request]
       (def ro request)
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (let [ses (cku/get-cookie-from-request request)
                    loc-data (dbc/get-location-data-from-session ses)]
                (if (and ses (dbc/location-exists? zip))
                  (if (dbc/associate-session-and-zip zip ses)
                    (str loc-data)
                    nil)
                  (let [loc-data (loc/get-location-data zip)]
                    (dbc/associate-session-and-location-data loc-data ses)
                    (loc/get-location-value loc-data :zip))))})

  (GET "/getzip" [lat long :as req]
       "this route returns a zipcode when given lat and longitude"
       (def ro req)
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (let [ses (cku/get-cookie-from-request req)
                    loc-data (dbc/get-location-data-from-session ses)
                    zip (-> loc-data
                            dbu/json-string-to-map
                            (get "zip"))]
                (if zip
                  (str loc-data)
                  (let [data (loc/get-location-data lat long)]
                    (dbc/associate-session-and-location-data data ses)
                                        ;(loc/get-location-value data :zip)
                    (dbc/get-location-data-from-session ses))))})

  (GET "/getUserFromSession" request
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (str (->> (cku/get-cookie-from-request request)
                        dbc/get-user-from-session-id))})

  (GET "/newPost" [content parent :as request]
       (def ro request)
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (let [ses (cku/get-cookie-from-request request)
                    user (dbc/get-user-from-session-id ses)
                    location-fk (dbc/get-location-from-session ses)]
                (if (not (-> content .trim .isEmpty))
                  (-> (dbc/create-new-post user content location-fk (if parent (Integer. parent) nil))
                      dbc/get-post-by-id)))})

  (GET "/newReplyPost" [content parent :as request]
       (def ro request)
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (let [ses (cku/get-cookie-from-request request)
                    user (dbc/get-user-from-session-id ses)
                    location-fk (dbc/get-location-from-session ses)]
                (if (not (-> content .trim .isEmpty))
                  (-> (dbc/create-reply-post<! dbc/db-spec
                                               {:user user
                                                :content content
                                                :location location-fk
                                                :parent parent})
                      first
                      :pst_id_pk
                      dbc/get-post-by-id)))})

  (GET "/deletePost" request
       (def ro request)
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (let [user-id (->> (cku/get-cookie-from-request request)
                                 dbc/get-user-from-session-id)
                    post-id (-> request :query-string (.split "=") (get 1))]
                (if (= user-id (dbc/get-user-id-from-post-id post-id))
                  (do (dbc/delete-post post-id)
                      (str post-id))
                  (str -1)))})

  (GET "/sessionSync" [cookie]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :cookies {"yapp-session" {:value (if (dbc/session-exists? cookie)
                                        cookie
                                        (dbc/create-session))
                               :max-age (* 60 24 30 365)}}
     :body cookie})

  (comment
    (GET "/*" request
         (def ro request)
         {:status 307
          :headers {"Content-Type" "application/json"}
          :body (let [short (:* (:route-params request))]
                  (try 
                    (dbc/long-link short)
                    (catch Exception ex "Link does not exist or has expired")))}))

  (POST "/share" [lat long link content :as request]
        (def ro request)
        (let [location-data (loc/get-location-data lat long)]
          (print location-data)))

  (route/resources "/")
  (route/not-found (views/not-found)))

(def app
  (-> app-routes
      wrap-json-body
      wrap-json-response
      (wrap-defaults site-defaults)
      (wrap-cors :access-control-allow-origin [#".*"] :access-control-allow-methods [:post])))

(defn -main [& [port]]
  (dbc/checkup)
  (let [port (Integer. (or (System/getenv "PORT") port 5000))]
    (jetty/run-jetty (handler/site #'app) {:port port :join? false})))
