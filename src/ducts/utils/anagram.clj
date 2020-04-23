;; fucking around and i made this for fun

(ns ducts.utils.anagram
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def dictionary (into #{} (-> (io/resource "../resources/dictionary")
                              slurp
                              (str/split #"\n"))))



(defn vec-remove
  "remove elem in coll"
  [pos coll]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))

(defn get-all-words
  ([^String word] (get-all-words (conj (into [] word) \space) 0))
  ([^clojure.lang.PersistentVector word-vector ^Integer index]
   (if (and word-vector
            (< index (count word-vector)))
     (for [char word-vector]
       (get-all-words (vec-remove index word-vector)
                      (inc index)))
     word-vector)))
