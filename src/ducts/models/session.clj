(ns ducts.models.session
  (:require [clojure.spec.alpha :as s]))

(s/def ::ses-id-pk int?)
(s/def ::ses-id (s/and string? #(< (count %) 64)))



  
