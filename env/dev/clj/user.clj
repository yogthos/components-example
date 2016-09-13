(ns user
  (:require [mount.core :as mount]
            [components-example.figwheel :refer [start-fw stop-fw cljs]]
            components-example.core))

(defn start []
  (mount/start-without #'components-example.core/repl-server))

(defn stop []
  (mount/stop-except #'components-example.core/repl-server))

(defn restart []
  (stop)
  (start))


