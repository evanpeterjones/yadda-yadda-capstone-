(defproject ducts "1.0.0-SNAPSHOT"
  :description "Ducts API Server"
  :url "http://ducts.herokuapp.com"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.4.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring/ring-json "0.4.0"]
                 [clojure.jdbc/clojure.jdbc-c3p0 "0.3.1"]
;;                 [ring-cors "0.1.13"]
                 [environ "1.0.0"]
                 [org.clojure/java.jdbc "0.3.5"]
                 [org.postgresql/postgresql "9.4-1201-jdbc4"]
                 [hiccup "1.0.5"]
                 [yesql "0.5.3"]]
  :min-lein-version "2.0.0"
  :source-paths ["src"]
  :resource-paths ["resources"]
  :plugins [[environ/environ.lein "0.3.1"]
            [lein-ring "0.12.5"]]
  :ring {:handler ducts.web/application
         :auto-reload? true
         :auto-refresh? false}
  :hooks [environ.leiningen.hooks]
  :main ducts.web
  :aot :all
  :uberjar-name "ducts-standalone.jar"
  :profiles {:production {:env {:production true}}})

