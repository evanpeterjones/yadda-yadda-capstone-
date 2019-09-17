(ns ducts.api
   (:use [ring.util.response])
   (:require [compojure.core :as core] ;; [defroutes GET POST]
             [compojure.route :as route]
             [compojure.handler :as handler]
             [compojure.response :only [Sendable]]
             [compojure.api.sweet :refer :all]
             [ring.util.http-response :as rsp]
             [schema.core :as s]
             [ducts.views :as views]
             [db-connection :as dbc]
             [clojure.java.jdbc :as jdbc])
   (:gen-class))

(s/def date-schema (s/either string? nil?))

(s/defschema User
  {:usr_id_pk s/Int
   :usr_username s/Str
   :usr_pwd_fk {s/Int nil}
   :usr_email s/Str
   :usr_createdon date-schema
   :usr_lastlogin date-schema})

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

(core/defroutes api-routes 
  (api
   {:swagger
    {:ui "/swagger/"
     :spec "/swagger.json"
     :data {:info {:title "Yadda-Ducts"
                   :description "The plumbing for a social media site"}
            :tags [{:name "Yadda API",
                    :description "An API for a social media site"}]}}}

   (context "/api" []
            :tags ["Yadda API"]

            (GET "/plus" [x y]
                 :return {:result Long}
                 :query-params [x :- Long, y :- Long]
                 :summary "adds two numbers together"
                 (rsp/ok {:result (+ x y)}))
            (GET "/db" []
                 :return User
                 :summary "poll to create a user"
                 (rsp/ok {:usr_id_pk 1, :usr_username "evan", :usr_pwd_fk nil, :usr_email "evanpeterjones@gmail.com", :usr_createdon #inst "2019-09-03T04:00:00.000-00:00", :usr_lastlogin nil})))))
