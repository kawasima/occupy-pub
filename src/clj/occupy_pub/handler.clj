(ns occupy-pub.handler
  (:require [compojure.core :refer [defroutes routes wrap-routes context GET]]
            [compojure.route :as route]
            [ring.util.response :refer [content-type]]
            [hiccup.middleware :refer [wrap-base-url]]
            [occupy-pub.certificate :as certificate]
            [occupy-pub.middleware :as middleware]
            [occupy-pub.jpa :as jpa]
            [occupy-pub.style :as style]
            (occupy-pub.routes [admin :refer [admin-routes]]
                               [user :refer [user-routes]]
                               [signup :refer [signup-routes]]
                               )
            [environ.core :refer [env]]
            [clojure.tools.nrepl.server :as nrepl])
  (:import [org.jboss.weld.environment.se Weld]
           [java.io File]))

(defroutes base-routes
  (GET "/css/occupy-pub.css" []
    (-> {:body (style/build)}
        (content-type "text/css")))
  (route/resources "/")
  (route/not-found "Not Found"))

(def weld (Weld.))

(defn init []
  (.initialize weld)
  (jpa/init-serial-number))

(defn destroy []
  (.shutdown weld))

(def app-base
  (routes
   (context "/admin" []
     (-> #'admin-routes
         (wrap-routes middleware/wrap-csrf)
         (wrap-routes wrap-base-url "/admin")))
   (context "/user" []
     (wrap-routes #'user-routes middleware/wrap-csrf))
   (context "/signup" [] signup-routes
     ;(wrap-routes #'signup-routes middleware/wrap-csrf)
            )
   #'base-routes))

(def app (middleware/wrap-base #'app-base))

