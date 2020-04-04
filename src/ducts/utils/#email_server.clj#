(ns ducts.utils.email-server
  (:require [sendgrid.core :as sg]))

(def api-token (or (System/getenv "SENDGRID_API_KEY") "Bearer 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"))

(defn yapp-send-email [email message]
  (sg/send-email {:api-token api-token
                  :from "yapp@internetizens.net"
                  :to email
                  :subject "YAPP Verify Your Email"
                  :content {:type "text/html"
                            :value message}}))

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

 
