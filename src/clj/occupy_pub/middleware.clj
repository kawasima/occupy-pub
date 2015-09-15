(ns occupy-pub.middleware
  (:require [environ.core :refer [env]]
            [clojure.java.io :as io]
       
            [ring.util.response :refer [redirect]]
            [ring.middleware.reload :as reload]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            [ring.middleware.session-timeout :refer [wrap-idle-session-timeout]]
            [ring.middleware.session.memory :refer [memory-store]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [clj-jpa.middleware :refer [wrap-entity-manager wrap-transaction]]
            [clj-jpa.entity-manager :as em])
  (:import [java.util Base64]
           [org.bouncycastle.asn1.x509 X509Name]
           [net.unit8.occupypub.model User]))


(defn wrap-dev [handler]
  (if (env :dev)
    (-> handler
        reload/wrap-reload)
    handler))

(defn get-header [request header-name]
  (some->> (:headers request)
           (filter #(.equalsIgnoreCase header-name (key %)))
           first
           val))

(defn- base64-decode [encoded]
  (String. (.. (Base64/getDecoder) (decode encoded))))

(defn- authorize [request]
  (when-let [decoded (some->> (get-header request "authorization")
                              (re-find #"^Basic (.+)$")
                              second
                              base64-decode)]
    (when-let [[dn passwd] (clojure.string/split decoded #":")]
      (when-let [email-address (first (try (.. (X509Name. dn) (getValues X509Name/EmailAddress))
                                           (catch Exception e nil)))]
        (first (em/search User :where (= :email-address email-address)))))))

(defn wrap-authorization [handler]
  (fn [request]
    (if (re-find #"^/(user|admin)(/|$)" (:uri request))
      (if-let [user (authorize request)]
        (handler (assoc request :identity user))
        {:status 401
         :headers {"WWW-Authenticate" (str "Basic realm=occupy-pub")}})
      (handler request))))

(defn wrap-csrf [handler]
  (wrap-anti-forgery handler))

(defn wrap-formats [handler]
  (wrap-restful-format handler {:formats [:json-kw :transit-json :transit-msgpack]}))

(defn wrap-base [handler]
  (-> handler
      wrap-dev
      (wrap-idle-session-timeout
       {:timeout (* 60 30)
        :timeout-response (redirect "/")})
      wrap-formats
      wrap-authorization
      wrap-transaction
      wrap-entity-manager
      (wrap-defaults
       (-> site-defaults
           (assoc-in [:security :anti-forgery] false)))))
