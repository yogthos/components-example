(ns components-example.widgets
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.string :as string]
            [re-com.core :refer [datepicker-dropdown]]
            [cljs-time.coerce :refer [from-date to-date]]
            [cljs-time.format :refer [parse unparse formatters]]
            [re-com.datepicker :refer [iso8601->date datepicker-dropdown-args-desc]]
            [components-example.model-view :as mv]))

(defn subscribe-doc [path]
  (rf/subscribe [:document path]))

(defmulti widget :type)

(defmethod widget :name-preview [{:keys [label path]}]
  (let [{:keys [first last]} @(subscribe-doc path)]
    [:label label ": "
     (->> [last first]
          (remove empty?)
          (string/join ", "))]))

(defmethod widget :date-preview [{:keys [label path]}]
  (let [date @(subscribe-doc path)]
    [:label label ": " (some->> date from-date (unparse (formatters :date)))]))

(defmethod widget :label [{:keys [label path]}]
  [:div [:label label ": " @(subscribe-doc path)]])

(defn parse-number [value]
  (let [number (js/parseFloat value)]
    (when-not (js/isNaN number)
      number)))

(defmethod widget :text-input [{:keys [label path numeric?]}]
  (r/with-let [value    (r/atom nil)
               focused? (r/atom false)]
    [:div.form-group
     [:label label]
     [:input.form-control
      {:type      :text
       :on-focus  #(do
                    (reset! value @(subscribe-doc path))
                    (reset! focused? true))
       :on-blur   #(do
                    (rf/dispatch
                      [:save path
                       (if numeric? (parse-number @value) @value)])
                    (reset! focused? false))
       :value     (if @focused? @value @(subscribe-doc path))
       :on-change #(reset! value (-> % .-target .-value))}]]))

(defmethod widget :numeric-input [opts]
  [widget (assoc opts :type :text-input :numeric? true)])

(defmethod widget :name [{:keys [first-name last-name path]}]
  [:div
   [widget {:label first-name :type :text-input :path (conj path :first)}]
   [widget {:label last-name :type :text-input :path (conj path :last)}]])

(defmethod widget :datepicker [{:keys [label path]}]
  [:div
   [:label label]
   [:div
    [datepicker-dropdown
     :model (some-> @(subscribe-doc path) from-date)
     :show-today? true
     :format "dd MMM, yyyy"
     :on-change #(rf/dispatch [:save path (to-date %)])]]])

(defmethod widget :dropdown [{:keys [label path options placeholder]}]
  (r/with-let [open? (r/atom false)]
    [:div
     [:label label]
     [:div.dropdown
      (when @open? {:class "open"})
      [:button.btn.btn-secondary.dropdown-toggle
       {:on-click #(swap! open? not)}
       (or @(subscribe-doc path) placeholder)]
      [:div.dropdown-menu
       (for [option options]
         ^{:key option}
         [:a.dropdown-item
          {:on-click
           #(do
             (rf/dispatch [:save path option])
             (reset! open? false))}
          option])]]]))

(defmethod widget :default [{:keys [type path]}]
  [:p.error
   "no widget of type " type
   " found for path " (str path)])

(defn create-widget [view path]
  (let [opts ((mv/views view) path)]
    [widget (assoc opts :path path)]))
