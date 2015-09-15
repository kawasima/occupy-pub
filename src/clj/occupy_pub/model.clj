(ns occupy-pub.model
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]))

(defn validate-user [user]
  (b/validate user
              :country-name [[v/matches #"^[A-Za-z]*$"]]
              :province-name [[v/matches #"^[A-Za-z]*$"]]
              :locality-name [[v/matches #"^[A-Za-z]*$"]]
              :given-name [[v/required]]
              :sir-name   [[v/required]]
              :email-address [[v/required]
                              [v/email]]
              :common-name [[v/matches #"^[A-Za-z0-9_]*$"]]))

(defn validate-group [group]
  (b/validate group
              :name [[v/required]
                     [v/max-count 80]]
              :description [[v/required]
                            [v/max-count 255]]))

(defn populate [params param-ns]
  (->> params
       (map (fn [[k v]] [(keyword k) v]))
       (filter (fn [[k v]] (= (namespace k) (name param-ns))))
       (map (fn [[k v]] [(keyword (name k)) v]))
       (reduce #(assoc %1 (first %2) (second %2))  {})))
