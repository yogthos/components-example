(ns components-example.view
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe]]
            [components-example.widgets :refer [create-widget]]
            [components-example.validation :as v]))

(defn validator []
  (r/with-let [validation-status (r/atom nil)]
    [:div
     [:button.btn.btn-primary
      {:on-click #(reset! validation-status (v/validate @(subscribe [:document])))}
      "validate"]
     (when-let [results @validation-status]
       [:ul
        (for [[path result] results]
          ^{:key path}
          [:li (str path " " result)])])]))

(defn form-row [view path]
  [:div.row>div.col-md-12
   (create-widget view path)])

(defn demographics [view]
  [:div
   (form-row view [:demographics :mrn])
   (form-row view [:demographics :name])
   (form-row view [:demographics :dob])
   (form-row view [:demographics :address :province])])

(defn vitals [view]
  [:div
   (form-row view [:vitals :weight])
   (form-row view [:vitals :height])
   (form-row view [:vitals :bmi])])

