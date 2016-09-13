(ns components-example.routes.ws
  (:require [components-example.document :refer [update-document!]]
            [compojure.core :refer [defroutes GET POST]]
            [taoensso.sente :as sente]
            [mount.core :refer [defstate]]
            [taoensso.sente.server-adapters.immutant :refer [get-sch-adapter]]))

(defstate socket
  :start (sente/make-channel-socket! (get-sch-adapter) {}))

(defmulti handle-message :id)

(defmethod handle-message :chsk/uidport-open [_])

(defmethod handle-message :chsk/uidport-close [_])

(defmethod handle-message :document/update [{:keys [?data]}]
  (let [updated-paths (update-document! ?data)]
    (doseq [uid (-> @socket :connected-uids deref :any)]
      ((:send-fn @socket) uid [:document/update updated-paths]))))

(defstate router
  :start (sente/start-chsk-router! (:ch-recv @socket) handle-message)
  :stop (router))

(defroutes document-routes
  (GET "/chsk" req ((:ajax-get-or-ws-handshake-fn @socket) req))
  (POST "/chsk" req ((:ajax-post-fn @socket) req)))

