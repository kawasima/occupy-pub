(ns occupy-pub.view.signup-view
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.core :refer [html]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn layout [& body]
  (html5
   [:head
    [:meta {:charset "UTF-8"}]
    [:title "Occupy pub"]
    (include-css "//cdn.jsdelivr.net/semantic-ui/2.0.8/semantic.min.css"
                 "/css/occupy-pub.css")]
   [:body
    [:div.ui.fixed.inverted.menu
     [:div.ui.container
      [:div.header.item {:href "#"}
       "Occupy pub"]]]
    [:div.ui.main.container body]]))

(defn signup-new-view [signup coupon & {:keys [errors]}]
  (layout
   [:form.ui.form {:method "post"}
    (anti-forgery-field)
    [:h4.ui.dividing.header "Signup"]
    [:div.field
     [:label "Name"]
     [:div.two.fields
      [:div.field (when (:given-name errors)
                    {:class "error"})
       [:input {:type "text" :name "signup/given-name" :value (:given-name signup) :placeholder "Given name"}]]
      [:div.field (when (:sir-name errors)
                    {:class "error"})
       [:input {:type "text" :name "signup/sir-name" :value (:sir-name signup) :placeholder "Sir name"}]]]]
    [:div.field (when (:common-name errors)
                  {:class "error"})
     [:label "Account name (CN)"]
     [:input {:type "text" :name "signup/common-name"}]]
    [:div.field (when (:email-address errors)
                  {:class "error"})
     [:label "Email address"]
     [:input {:type "text" :name "signup/email-address"}]]
    
    [:div.field (when (:country-name errors)
                  {:class "error"})
     [:label "Country"]
     [:input {:type "text" :name "signup/country-name"}]]
    [:div.field (when (:province errors)
                  {:class "error"})
     [:label "Province"]
     [:input {:type "text" :name "signup/province-name"}]]
    [:div.field (when (:locality-name errors)
                  {:class "error"})
     [:label "Locality"]
     [:input {:type "text" :name "signup/locality-name"}]]
    [:div.field (when (:organization-name errors)
                  {:class "error"})
     [:label "Organization"]
     [:input {:type "text" :name "signup/organization-name"}]]
    [:div.field (when (:organization-unit-name errors)
                  {:class "error"})
     [:label "Organization unit"]
     [:input {:type "text" :name "signup/organization-unit-name"}]]
    
    [:div.field
     [:label "Coupon code"]
     [:input {:type "text" :name "coupon/code" :value (:code coupon)}]]
    [:button.ui.button {:type "submit"} "Signup"]]))

(defn signup-beginning-view [signup]
  (layout
   [:form.ui.form {:method "post"}
    (anti-forgery-field)
    [:div.field
     [:label "CA password"]
     [:input {:type "password" :name "ca-password" :value ""}]]
    [:button.ui.button {:type "submit"} "Signup as system administrator"]]))
