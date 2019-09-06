(ns ducts.api
   (:use compojure.core
         ring.util.response)
   (:require [compojure.route :as route]
             [compojure.handler :as handler]
;;             [compojure.api.sweet :refer :all]
             [ring.util.http-response :refer :all]
             [schema.core :as s]
             [ducts.views :as views]
             [db-connection :as dbc]
             [clojure.java.jdbc :as jdbc])
   (:gen-class))

(s/defschema User
  {:usr-id-pk s/Int
   :usr-username s/Str
   :usr-email s/Str})

(s/defschema Session
  {:ses-id-pk s/Int
   :ses-id s/Int
   :ses-user-id-fk User})

(s/defschema Post
  {:pst-id s/Int
   :pst-usr-id-fk User
   :pst-loc-fk s/Str
   :pst-content s/Str
   :pst-time s/Str
   :pst-edittime s/Str})

(def api-routes
  (api
   (GET "/hello" []
        :query-params [name :- String]
        (ok {:message (str "Hello, " name)}))))

(def api-routes
           (api
            {:swagger
             {:ui "/"
              :spec "/swagger.json"
              :data {:info {:title "Yadda-Ducts"
                            :description "The plumbing for a social media site"}
                     :tags [{:name "Yadda API",
                             :description "An API for a social media site"}]}}}

            (context "/api" []
                     :tags ["Yadda API"]

                     (GET "/topPosts" []
                          :return {:result {:posts [& Post]}} ; not sure how to declare an array of Post objects
                          :query-params [location :- String]
                          :summary "given a location, load posts from that location"
                          :body (response (dbc/topPosts location)))
                     (GET "/createuser" []
                          :return {:result {:created boolean}}
                          :query-params [username :- String,
                                         email :- String]
                          :summary "poll to create a user"
                          :body (dbc/create-user! {:username username :email email}))
                     (GET "/db" []
                          :return {:result {:created boolean}}
                          :query-params [username :- String,
                                         email :- String]
                          :summary "poll to create a user"
                          ;(dbc/tick)
                          {:status 200
                           :headers {"Content-Type" "text/plain"}
                           :body (str "Ticks: " (first (jdbc/query ["select count(*) from ducts"])))}))))
