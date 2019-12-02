(ns ducts.models.session
  (:require [clojure.spec.alpha :as s]
            [ducts.models.user :as user]))

(s/def ::ses-id-pk int?)

(def Session
  {:ses-id-pk ::ses-id-pk
   :ses-id (s/and string? #(< (count %) 64))
   :ses-user-id-fk :user/usr-id-pk
   })


