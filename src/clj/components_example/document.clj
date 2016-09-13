(ns components-example.document)


(defonce document (ref {}))

(defn bmi [weight height]
  (when (and weight height (pos? height))
    (/ weight (* height height))))

(defn bmi-rule [doc]
  (let [weight (get-in doc [:vitals :weight])
        height (get-in doc [:vitals :height])]
    [{:path  [:vitals :bmi]
      :value (bmi weight height)}]))

(def rules
  {[:vitals :weight] bmi-rule
   [:vitals :height] bmi-rule})

(defn run-rules [doc {:keys [path]}]
  (when-let [rule (rules path)]
    (rule doc)))

(defn update-document! [{:keys [path value] :as path-value}]
  (dosync
    (let [current-document (alter document assoc-in path value)
          updated-paths    (run-rules current-document path-value)]
      (doseq [{:keys [path value]} updated-paths]
        (alter document assoc-in path value))
      (into [path-value] updated-paths))))
