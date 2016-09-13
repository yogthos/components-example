(ns components-example.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [components-example.layout :refer [error-page]]
            [components-example.routes.home :refer [home-routes]]
            [components-example.routes.ws :refer [document-routes]]
            [compojure.route :as route]
            [components-example.env :refer [defaults]]
            [mount.core :as mount]
            [components-example.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    #'document-routes
    (middleware/wrap-csrf #'home-routes)
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
