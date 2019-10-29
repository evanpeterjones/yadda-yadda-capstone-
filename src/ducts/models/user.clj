(ns ducts.models.user
  (:require [clojure.spec.alpha :as s]
            [ducts.models.shared :as shared]))

(s/def ::usr-id-pk int?)

(def User 
  {:usr-id-pk ::usr-id-pk
   :usr-username (s/and string? #(<= (count %) 50))
   :usr-pwd-fk int?
   :usr-email :shared/email
   :usr-createdon :shared/date})
