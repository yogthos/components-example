(ns components-example.subscriptions
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
  :document
  (fn [db [_ path]]
    (let [doc (:document db)]
      (if path (get-in doc path) doc))))
