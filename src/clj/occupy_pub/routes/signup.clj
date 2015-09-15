(ns occupy-pub.routes.signup
  (:require [compojure.core :refer [defroutes routes GET POST]]
            [ring.util.response :refer [redirect response content-type]]
            [clj-jpa.entity-manager :as em]
            [occupy-pub.certificate :as certificate]
            [occupy-pub.model :as model :refer :all]
            [occupy-pub.view.signup-view :refer :all])
  (:import [net.unit8.occupypub.model User Cert]))

(defroutes signup-routes
  (GET "/" {params :params :as req}
    (signup-new-view {} (select-keys params [:code])))

  (POST "/" {params :params :as req}
    (let [signup (model/populate params 'signup)
          coupon (model/populate params 'coupon)
          [result map] (validate-user signup)]
      (if-let [errors (:bouncer.core/errors map)]
        (signup-new-view signup coupon :errors errors)
        (if (= 0 (count (em/search User)))
          (-> (redirect "beginning") 
              (assoc-in [:session :signup]
                        (assoc signup :admin true)))))))
  (GET "/beginning" {{signup :signup} :session}
    (signup-beginning-view signup))
  (POST "/beginning" {params :params session :session :as req}
    (let [user (em/merge User (:signup session))
          cert (certificate/generate-certificate (:entity (meta user)) 30)]
      (em/merge Cert cert)
      (redirect "/admin/"))))
