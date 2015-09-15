(ns occupy-pub.routes.user
  (:require [compojure.core :refer [defroutes routes GET POST]]
            [ring.util.response :refer [redirect]]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [occupy-pub.jpa :as jpa]
            [occupy-pub.view.user-view :refer :all])
  (:import [net.unit8.occupypub.model User]))

(defroutes user-routes
  (GET "/" {:keys [identity] :as req}
    (user-view identity))
  (GET "/devices/new" []
    (device-new-view))
  (POST "/devices/new" []))

