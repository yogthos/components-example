(ns components-example.model-view
  (:require [components-example.model :refer [model]]))

(def demographics-form
  {[:demographics :mrn]
   {:label "MRN"
    :type :text-input}

   [:demographics :name]
   {:first-name "first name"
    :last-name  "last name"
    :type       :name}

   [:demographics :dob]
   {:label "date of birth"
    :type  :datepicker}

   [:demographics :address :province]
   {:type        :dropdown
    :label       "province"
    :placeholder "select a province"
    :options     (-> [:demographics :address :province]
                     model
                     vals
                     first
                     sort)}})

(def vitals-form
  {[:vitals :weight]
   {:label "weight"
    :type  :numeric-input}

   [:vitals :height]
   {:label "height"
    :type  :numeric-input}

   [:vitals :bmi]
   {:label "BMI"
    :type  :label}})

(defn preview [form]
  (->> form
       (map (fn [[k v]]
              [k (case (:type v)
                   :name (assoc v :type :name-preview
                                  :label "name")
                   :datepicker (assoc v :type :date-preview)
                   (assoc v :type :label))]))
       (into {})))

(def views
  {:demographics-form    demographics-form
   :demographics-preview (preview demographics-form)
   :vitals-form          vitals-form
   :vitals-preview       (preview vitals-form)})

