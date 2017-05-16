(ns future-app.android.secondpage
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [future-app.events]
            [future-app.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def navigator (r/adapt-react-class (.-Navigator ReactNative)))

(def logo-img (js/require "./images/cljs.png"))


(defn alert [title]
      (.alert (.-Alert ReactNative) title))

(defn SecondPage[{navigator :navigator route :route}]
  (let [greeting (subscribe [:get-greeting])]
    (fn []
      (println "ciao " (js->clj (.getCurrentRoutes navigator)))
      [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
       [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} (.-id route)]
       [image {:source logo-img
               :style  {:width 80 :height 80 :margin-bottom 30}}]
       [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                             :on-press #(.push navigator #js {:id "animation"})}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "press me"]]])))
