(ns occupy-pub.routes.admin
  (:require [compojure.core :refer [defroutes routes GET POST]]
            [compojure.coercions :refer :all]
            [ring.util.response :refer [redirect]]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [clj-jpa.entity-manager :as em]
            [occupy-pub.model :refer :all]
            [occupy-pub.view.admin-view :refer :all])
  (:import [net.unit8.occupypub.model User Group Membership GroupRole]
           [cljjpa EntityFactory]))

(defroutes admin-routes
  (GET "/users" {:as req}
    (let [users (em/search
                 User
                 :jpql "SELECT u FROM User u LEFT JOIN FETCH u.certList c")]
      (user-list-view users)))
  
  (GET "/users/new" []
    (user-new-view nil))
  
  (POST "/users/new" {params :params :as req}
    (let [user (populate params 'user)
          [result map] (validate-user user)]
      (if-let [errors (:bouncer.core/errors map)]
        (user-new-view user :errors errors)
        (do
          (em/merge User user)
          (redirect "/admin/users" :see-other)))))
  
  (POST "/user/:id/delete" [])
  
  (GET "/groups" {:as req}
    (let [groups (em/search Group)]
      (group-list-view groups)))
  
  (GET "/groups/new" []
    (group-new-view nil))
  
  (POST "/groups/new" {params :params :as req}
    (let [group (populate params 'group)
          [result map] (validate-group group)]
      (if-let [errors (:bouncer.core/errors map)]
        (group-new-view group :errors errors)
        (do
          (em/merge Group group)
          (redirect "/admin/groups" :see-other)))))
  
  (GET "/group/:id" [id :<< as-int]
    (let [group (em/find Group id)
          users (em/search User
                           :jpql "SELECT u FROM User u LEFT JOIN u.membershipList m ON m.group.id = :groupId
                                  GROUP BY u.id HAVING COUNT(m) = 0"
                           :params {:groupId id})]
      (group-detail-view group users)))

  (POST "/group/:id/add" [id :<< as-int :as {{:keys [members]} :params}]
    (let [group (em/find Group id)]
      (doseq [member (if (seq? members) members [members])]
        (let [membership (em/merge Membership {:group group
                                      :user (em/find User (Long/parseLong member))
                                               :group-role GroupRole/DEVELOPER})])))
    (redirect (str "/admin/group/" id)))

  (POST "/group/:group-id/remove/:user-id" [group-id :<< as-int user-id :<< as-int]
    (let [memberships (em/search Membership
                                 :where (and (= :group-id group-id)
                                             (= :user-id  user-id)))]
      (doseq [membership memberships]
        (em/remove membership)))
    (redirect (str "/admin/group/" group-id)))

  (GET "/" {}
    (redirect "users")))
