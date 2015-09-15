(ns occupy-pub.certificate
  (:require [environ.core :refer [env]]
            [occupy-pub.jpa :as jpa])
  (:import [net.unit8.occupypub.cert CertificationAuthority]
           [java.time LocalDate]
           [java.io File]))

(def ca-certificate-file (.. (File. (or (:ca-certificate-file env)
                                        "dev-resources/certs/occupy-ca.pem"))
                             toPath))

(def ca-key-file (.. (File. (or (:ca-certificate-file env)
                                "dev-resources/private/occupy-ca-key.pem"))
                     toPath))

(def token-chars "abcdefghijklmnopqrstuvwxyz0123456789")

(defn- rand-string [characters n]
  (->> (fn [] (rand-nth characters))
       repeatedly
       (take n)
       (apply str)))

(defn generate-certificate [user expire-days]
  (let [ca (CertificationAuthority. ca-certificate-file ca-key-file)
        keypair (.generateKey ca)
        csr (.generateCSR ca user keypair)
        serial (jpa/serial-number)
        cert (.generateCertificate ca csr serial expire-days)]
    {:serial-no (int serial)
     :private-key (.. keypair getPrivate getEncoded)
     :client-cert (.. cert getEncoded)
     :expires (.. (LocalDate/now) (plusDays expire-days))
     :token (rand-string token-chars 20)
     :active true
     :user user}))
