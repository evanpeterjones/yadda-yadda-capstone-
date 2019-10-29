(ns ducts.views)

(defn web-app []
  (slurp "resources/index.html"))

(defn not-found []
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (slurp "resources/404.html")})