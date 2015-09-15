(ns occupy-pub.view.user-view
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.core :refer [html]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [occupy-pub.view.layout :refer [base-layout]]))

(defn layout [& body]
  (base-layout
   (list nil)
   body))

(defn user-view [user]
  (layout
   [:div.ui.grid
    [:div.ui.sixteen.wide.column
     [:div.ui.info.message
      [:div.header "Continue?"]]]
    [:div.ui.four.wide.column
     [:div.ui.card
      [:div.image
       [:img {:src "//www.gravatar.com/avatar/00000?d=mm&s=300"}]]
      [:div.content
       [:a.header (str (:sir-name user) (:given-name user))]
       [:div.meta
        [:span.email (:email-address user)]]]
      [:div.extra.content
        (get-in user [:cert-list 0 :expires])]]]
    [:div.ui.twelve.wide.column
     [:div.ui.segment
      [:h3.ui.top.header "Access log"]]
     [:div.ui.segment
      [:h3.ui.top.header "Devices"]]]
    ]
   ))

(defn device-new-view [device & {:keys [errors]}]
  (layout
   [:form.ui.form {:method "post"}
    (anti-forgery-field)
    [:h4.ui.dividing.header "New device"]
    [:div.field (when (:device-name errors)
                  {:class "error"})
     [:label "Device name"]
     [:input {:type "text" :name "user-device/device-name"}]]
    [:div.field (when (:serial-code errors)
                  {:class "error"})
     [:label "Serial code"]
     [:input {:type "text" :name "user-device/serial-code"}]]
    [:button.ui.button {:type "submit"} "Save"]]))

