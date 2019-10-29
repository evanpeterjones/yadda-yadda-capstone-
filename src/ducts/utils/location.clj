(ns ducts.utils.location
  ;; server resource to generate db location data from a zipcode
  (:require [clojure.string]
            [clojure.data.json :as json]
            [clj-http.client :as client]))

(def gmap-url "https://maps.googleapis.com/maps/api/geocode/json?latlng=")
(def gmap-end "&key=AIzaSyAxjvat9ISImf_NycNDUhFHqK9anG29rPs")

(defn -make-maps-request [lat long]
  (let [url (str gmap-url lat "," long gmap-end)]
    (client/get url {:accept :json})))

(defn -validate [m type]
  "JESUS FUCKING CHRIST DEBUGGING THIS ONE LINE TOOK FOR FUCKING EVER"
  (filter (fn [obj] (some #(= % type) (get obj "types"))) m))

(def ops {:zip "postal_code"
          :city "locality"
          :state "administrative_area_level_1"
          :county "administrative_area_level_2"
          :road "route"
          :street "street_number"})

(defn get-location-value [m opt]
  (-> m
      (-validate (opt ops))
      first
      (get (if (= opt :state)
             "short_name" "long_name"))))

(defn get-location-data
  ([lat long]
   (let [res (-make-maps-request lat long)
         js (json/read-str (:body res))]
     (->
      js
      (get "results")
      first
      (get "address_components")))))

(comment
  (defn generate-location-data [zip-code]
    "query to generate location alias and insert into DB"
    (let [alias (dbc/get-location-alias zip-code)]
      (or alias
          ())))

  (defn authenticate-location [zip]
    "given a location, determine if the location is valid"
    (let [loc-row (dbc/get-location zip)] ;; TODO: need location queries
      (if-not loc-row
        (authenticate-location (generate-location-data zip))
        loc-row))))
