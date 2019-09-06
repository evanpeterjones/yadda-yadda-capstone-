(ns ducts.views)

(defn web-app []
  (slurp "resources/index.html")) ;; TODO: write front-end

(comment (defn splash []
           {:status 200
            :headers {"Content-Type" "text/plain"}
            :body (welcome-page)}))
