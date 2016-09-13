(ns components-example.model
  (:require [schema.core :as s]))

(def Name
  {:first s/Str
   :last  s/Str})

(def demographics
  {[:demographics :mrn]
   s/Str

   [:demographics :name]
   Name

   [:demographics :dob]
   #?(:clj java.util.Date
      :cljs js/Date)

   [:demographics :address :province]
   (s/enum "AB" "BC" "MB" "NB" "NL" "NS" "NT" "NU" "ON" "PE" "QC" "SK" "YT")})

(def vitals
  {[:vitals :weight]
   s/Num

   [:vitals :height]
   s/Num

   [:vitals :bmi]
   s/Num})

(def model
  (merge
    demographics
    vitals))

