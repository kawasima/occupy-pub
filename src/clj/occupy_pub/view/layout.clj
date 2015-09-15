(ns occupy-pub.view.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.core :refer [html]]
            [hiccup.util :refer [with-base-url]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn base-layout [menu & body]
  (html5
   [:head
    [:meta {:charset "UTF-8"}]
    [:title "Occupy pub"]
    (include-css "//cdn.jsdelivr.net/semantic-ui/2.1.4/semantic.min.css")
    [:link {:href "/css/occupy-pub.css" :rel "stylesheet" :type "text/css"}]]
   [:body
    [:div.ui.fixed.inverted.menu
     [:div.ui.container
      [:div.header.item {:href "#"}
       "Occupy pub"]
      menu]]
    [:div.ui.main.container body]]))



