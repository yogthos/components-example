(ns components-example.core-test
  (:require [cljs.test :refer-macros [is are deftest testing use-fixtures]]
            [reagent.core :as reagent :refer [atom]]
            [components-example.core :as rc]))

(deftest test-home
  (is (= true true)))

