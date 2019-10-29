(ns ducts.models.shared
  (:require [clojure.spec.alpha :as s]))

(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")

(s/def ::email (s/and #(re-matches email-regex %) string?))
(s/def ::date (s/and true true))                     
