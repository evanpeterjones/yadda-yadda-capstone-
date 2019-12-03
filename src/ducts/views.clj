(ns ducts.views)

(defn web-app []
  (slurp "resources/index.html"))

(defn not-found []
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (slurp "resources/404.html")})

(defn email-verified []
  (slurp "resources/email_verified.html"))

(defn verify-email [url]
  (clojure.string/replace
   (slurp "resources/email.html")
   #"%TARGETURL%"
   (str "https://internetizens.net/verifyEmail?key=" url)))
