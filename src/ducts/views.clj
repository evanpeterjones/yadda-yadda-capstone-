(ns ducts.views)

(defn web-app []
  (slurp "resources/index.html"))

(comment (defn splash []
           {:status 200
            :headers {"Content-Type" "text/plain"}
            :body (welcome-page)}))

(defn not-found []
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (slurp "resources/404.html")})
