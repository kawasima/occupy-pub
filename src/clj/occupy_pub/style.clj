(ns occupy-pub.style
  (:use [garden.core :only [css]]
        [garden.units :only [px em]]))

(def styles
  [[:.main.container {:margin-top (em 7)}]])

(defn build []
  (css {:pretty-print? false} styles))
