(ns future-app.events
  (:require
   [re-frame.core :refer [reg-event-db after]]
   [clojure.spec :as s]
   [future-app.db :as db :refer [app-db nav]]))

;; -- Interceptors ------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/master/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db [event]]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check after " event " failed: " explain-data) explain-data)))))

(defn nav-pop []
  (if (and @nav (> (.-length (.getCurrentRoutes @nav)) 1))
      (.pop @nav)
      ))
(defn nav-push [value]
  (.push @nav #js {:id value}))

(def validate-spec
  (if goog.DEBUG
    (after (partial check-and-throw ::db/app-db))
    []))

;; -- Handlers --------------------------------------------------------------

(reg-event-db
 :initialize-db
 validate-spec
 (fn [_ _]
   app-db))

(reg-event-db
 :set-greeting
 validate-spec
 (fn [db [_ value]]
   (assoc db :greeting value)))

   (reg-event-db
     :nav-pop
     (fn [nav [_]]
       (nav-pop)
       nav))

 (reg-event-db
   :nav-push
   (fn [nav [_ value]]
     (nav-push value)
     nav))
