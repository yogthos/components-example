(ns components-example.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [components-example.core-test]))

(doo-tests 'components-example.core-test)

