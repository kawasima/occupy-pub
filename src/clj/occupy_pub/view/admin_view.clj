(ns occupy-pub.view.admin-view
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.core :refer [html]]
            [hiccup.util :refer [url]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [occupy-pub.view.layout :refer [base-layout]]))

(defn layout [& body]
  (base-layout
   (list [:a.item {:href (url "/users")} "Users"]
         [:a.item {:href (url "/groups")} "Groups"])
   body))

(defn user-list-view [users]
  (layout
   [:div.ui.grid
    [:div.row
     [:div.column
      [:table.ui.table
       [:thead
        [:tr
         [:th "Email address"]
         [:th "Device name"]
         [:th "Expires"]]]
       [:tbody
        (for [user users]
          [:tr
           [:td (str (:sir-name user) (:given-name user)
                     " (" (:email-address user) ")") ]
           [:td (:deviceList user)]
           [:td (get-in user [:cert-list 0 :expires])]])]]]]
    [:div.row
     [:div.column
      [:a.ui.button
       {:href "/admin/users/new"} [:i.plus.icon] "New"]]]]))

(defn user-new-view [user & {:keys [errors]}]
  (layout
   [:form.ui.form {:method "post"}
    (anti-forgery-field)
    [:h4.ui.dividing.header "New user"]
    [:div.field
     [:label "Name"]
     [:div.two.fields
      [:div.field (when (:given-name errors)
                    {:class "error"})
       [:input {:type "text" :name "user/given-name" :value (:given-name user) :placeholder "Given name"}]]
      [:div.field (when (:sir-name errors)
                    {:class "error"})
       [:input {:type "text" :name "user/sir-name" :value (:sir-name user) :placeholder "Sir name"}]]]]
    [:div.field (when (:common-name errors)
                  {:class "error"})
     [:label "Account name (CN)"]
     [:input {:type "text" :name "user/common-name"}]]
    [:div.field (when (:email-address errors)
                  {:class "error"})
     [:label "Email address"]
     [:input {:type "text" :name "user/email-address"}]]
    
    [:div.field (when (:country-name errors)
                  {:class "error"})
     [:label "Country"]
     [:input {:type "text" :name "user/country-name"}]]
    [:div.field (when (:province errors)
                  {:class "error"})
     [:label "Province"]
     [:input {:type "text" :name "user/province-name"}]]
    [:div.field (when (:locality-name errors)
                  {:class "error"})
     [:label "Locality"]
     [:input {:type "text" :name "user/locality-name"}]]
    [:div.field (when (:organization-name errors)
                  {:class "error"})
     [:label "Organization"]
     [:input {:type "text" :name "user/organization-name"}]]
    [:div.field (when (:organization-unit-name errors)
                  {:class "error"})
     [:label "Organization unit"]
     [:input {:type "text" :name "user/organization-unit-name"}]]
    
    [:div.field
     [:label "CA password"]
     [:input {:type "password" :name "ca-password"}]]
    [:button.ui.button {:type "submit"} "Save"]]))

(defn group-list-view [groups]
  (layout
   [:div.ui.grid
    [:div.row
     [:div.column
      [:table.ui.table
       [:thead
        [:tr
         [:th "Group name"]
         [:th "Description"]]]
       [:tbody
        (for [group groups]
          [:tr
           [:td [:a {:href (str "/admin/group/" (:id group))} (:name group)]]
           [:td (:description group)]])]]]]
    [:div.row
     [:div.column
      [:a.ui.button
       {:href "/admin/groups/new"} [:i.plus.icon] "New"]]]]))

(defn group-new-view [group & {:keys [errors]}]
  (layout
   [:form.ui.form {:method "post"}
    (anti-forgery-field)
    [:h4.ui.dividing.header "New group"]
    
    [:div.field (when (:name errors)
                  {:class "error"})
     [:label "Group name"]
     [:input {:type "text" :name "group/name"}]]

    [:div.field (when (:description errors)
                  {:class "error"})
     [:label "Description"]
     [:input {:type "text" :name "group/description"}]]
    [:button.ui.button {:type "submit"} "Save"]]))

(defn group-detail-view [group users]
  (layout
   [:div (:name group)]
   [:h3.ui.header (str "Members (" (count (:users group)) ")")]
   [:div.ui.grid
    [:div.twelve.wide.column
     [:table.ui.table
      [:thead
       [:th "User name"]
       [:th "Role"]
       [:th ""]]
      [:tbody
       (for [membership (:memberships group)]
         [:tr
          [:td (get-in membership [:user :common-name])]
          [:td (:group-role membership)]
          [:td [:form.ui.form {:method "post" :action (str "/admin/group/" (:id group) "/remove/" (get-in membership [:user :id]))}
                (anti-forgery-field)
                [:button.ui.button {:type "submit"} [:i.delete.icon]]]]])]]]
    [:div.four.wide.column
     [:form.ui.form {:method "post" :action (str "/admin/group/" (:id group) "/add")}
      (anti-forgery-field)
      [:select {:name "members" :rows "10" :multiple "multiple"}
       (for [user users]
         [:option {:value (:id user)} (str (:given-name user) " " (:sir-name user))])]
      [:button.ui.button {:type "submit"} "Add"]]]]
   ))
