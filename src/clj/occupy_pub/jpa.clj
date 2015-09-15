(ns occupy-pub.jpa
  (:require [clj-jpa.core :refer [with-entity-manager with-transaction]]
            [clj-jpa.entity-manager :as em])
  (:import [javax.persistence EntityManager LockModeType]
           [net.unit8.occupypub.model SerialNumber])
  (:refer-clojure :exclude [find remove count]))

(defn init-serial-number []
  (with-entity-manager
    (when-not (em/find SerialNumber 1)
      (with-transaction
        (em/merge SerialNumber {:id 1
                                :value BigInteger/ONE})))))

(defn serial-number []
  (with-entity-manager
    (let [sn (em/find SerialNumber 1)]
      (with-transaction
        (em/lock sn LockModeType/PESSIMISTIC_WRITE)
        (em/merge (update-in sn [:value] #(.add % BigInteger/ONE)))
        (:value sn)))))
