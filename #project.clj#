(defproject ducts "1.0.0-SNAPSHOT"
  :description "Ducts API Server"
  :url "http://ducts.herokuapp.com"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/data.json "0.2.6"]
                 [net.polyc0l0r/bote "0.1.0"]
                 [com.google.api-client/google-api-client "1.30.4"]
                 [clj-http "3.10.0"]
                 [clj-sendgrid "0.1.2"]
                 [compojure "1.4.0"]
                 [com.draines/postal "2.0.3"]
                 [com.layerware/hugsql "0.5.1"]
                 [org.clojure/data.json "0.2.6"]
                 [ring/ring-mock "0.4.0"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.5.0"]
                 [ring-cors "0.1.13"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [clojure.jdbc/clojure.jdbc-c3p0 "0.3.1"]
                 [metosin/compojure-api "1.1.12"]
                 [environ "1.0.0"]
                 [org.clojure/java.jdbc "0.3.5"]
                 [org.postgresql/postgresql "42.2.4"]
                 [hiccup "1.0.5"]
                 [com.mkyong.hashing/java-project "1.0"]
;                 [com.pluvius/readie "0.0.0"]
                 [twttr "3.2.2"]]
  :min-lein-version "2.0.0"
  :source-paths ["src"]
  :resource-paths ["resources"]

  :plugins [[environ/environ.lein "0.3.1"]
            [lein-ring "0.12.5"]]
  :ring {:handler ducts.web/application
         :auto-reload? true
         :auto-refresh? false}
  :main ducts.web
  :aot :all
  :uberjar-name "ducts-standalone.jar"
  :profiles {:production {:env {:production true}}})

