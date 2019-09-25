(defn into-test []
  (into {} (map #(vector % (int %)) "ProtoRepl")))
