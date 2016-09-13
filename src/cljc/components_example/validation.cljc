(ns components-example.validation
  (:require [schema.core :as s]
            [components-example.model :refer [demographics]]))

(defn validate [doc]
  (keep
    (fn [[path type]]
      [path
       (try
         (s/validate type (get-in doc path))
         #?(:cljs (catch js/Error e
                    (.-message e)))
         #?(:clj (catch Exception e
                   (.getMessage e))))])
    demographics))
