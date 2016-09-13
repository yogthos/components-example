(ns components-example.ws
  (:require [taoensso.sente :as sente]
            [re-frame.core :refer [dispatch]]
            [mount.core :refer [defstate]]))

(defstate socket
  :start (sente/make-channel-socket! "/chsk" {:type :auto}))

(defn update-value [path-value]
  ((:send-fn @socket) [:document/update path-value]))

(defmulti handle-message first)

(defmethod handle-message :document/update [[_ updated-paths]]
  (doseq [{:keys [path value]} updated-paths]
    (dispatch [:set-doc-value path value])))

(defmulti handle-event :id)

(defmethod handle-event :chsk/state [_])

(defmethod handle-event :chsk/handshake [_])

(defmethod handle-event :chsk/recv [{:keys [?data]}]
  (handle-message ?data))


(defstate router
  :start (sente/start-chsk-router! (:ch-recv @socket) handle-event))
