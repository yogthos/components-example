(ns components-example.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[components-example started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[components-example has shut down successfully]=-"))
   :middleware identity})
