(ns future-app.android.core
  (:require [reagent.core :as r :refer [atom]]
            [future-app.android.firstpage :as f]
            [future-app.android.secondpage :as s]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [future-app.events]
            [future-app.subs]))

(def ReactNative (js/require "react-native"))
(def navigator (r/adapt-react-class (.-Navigator ReactNative)))

(def app-registry (.-AppRegistry ReactNative))
(def BackHandler (.-BackAndroid ReactNative))


;(def nav (atom #js []))

 (.addEventListener back-android "hardwareBackPress" (fn [] (dispatch [:nav-pop])true )
; (.addEventListener BackHandler "hardwareBackPress" (fn [] (if (and @nav (> (.-length (.getCurrentRoutes @nav)) 1))
;                                                               (do (.pop @nav) true)
;                                                               false)))





(defn app-root []
  [navigator {:initial-route {:id "firstpage"}
              :render-scene (fn [route navigator]
                              (reset! nav navigator)
                              (case (.-id route)
                                "firstpage" (r/as-element [f/FirstPage {:navigator navigator :route route}])
                                "secondpage" (r/as-element [s/SecondPage {:navigator navigator :route route}])))}])

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "FutureApp" #(r/reactify-component app-root)))
