(ns components-example.handlers
  (:require [components-example.db :as db]
            [components-example.ws :as ws]
            [re-frame.core :refer [dispatch reg-event-db]]))

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (assoc db :page page)))

(reg-event-db
  :set-doc-value
  (fn [db [_ path value]]
    (assoc-in db (into [:document] path) value)))

(reg-event-db
  :save
  (fn [db [_ path value]]
    (ws/update-value {:path path :value value})
    db))
