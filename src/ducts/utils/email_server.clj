(ns ducts.utils.email-server
  (:require [bote.core :refer [create-smtp-server]]
            [postal.core :refer [send-message]]))

(def received (atom nil))

(def smtp-server (create-smtp-server #(reset! received %)
                                     :port 2526
                                     :enable-tls? true
                                     :require-tls? true))

;;(.start smtp-server)

(defn yapp-send-email [message]
  (send-message {:host "localhost"
                 :port 2526}
                {:from "foo@bar.com"
                 :to "evanpeterjones@gmail.com"
                 :subject "IMPORTANT!!!1!"
                 :body message}))

;(yapp-send-email "hello world!")

(def mail-host "smtp.gmail.com")
(def mail-port 587)
(def mail-user "evan.peter.jones@gmail.com")
(def mail-pass "Avogadro6.02")
(def mail-from "evanpeterjones@gmail.com")
(def mail-ssl true)

(comment
  (def dropsonde-server
    (mail-server
     mail-host
     mail-port
     mail-ssl
     mail-user
     mail-pass
     mail-from))

  (def email-subjects {:verification-email "Verify Your Email"})

  (defn send-user-email [user-email e-type url]
    (send-to dropsonde-server
             user-email
             "verify your email"
             (str "Click this link to verify your email " url))))

 
