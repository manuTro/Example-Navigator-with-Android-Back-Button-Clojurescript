(ns future-app.android.animation
  (:require [reagent.core :as r :refer [atom create-class]]
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
(def Animated (.-Animated ReactNative))
(def animated-value (.-Value Animated))
(def fadeAnim (new animated-value 0))
(def animatedView (r/adapt-react-class (.-View Animated)))
(def state  #js {:fadeAnim fadeAnim})


;;;;;;;;;;;;;;;;;;;;;;;;;with meta-data
; (def FadeInView
;   "with-meta works with def not with defn because takes the metadata of the first function"
;   (with-meta (fn []
;                [animatedView
;                 {:style {:width 250 :height 50 :background-color "powderblue" :opacity (.-fadeAnim state)}}
;                 [text {:style {:font-size 28 :text-align "center" :margin 10}} "Fadingin"]])
;              {:component-did-mount (fn [] (-> Animated
;                                               (.timing
;                                                (.-fadeAnim state) #js {:toValue 1})
;                                               (.start)))}))
;;;;;;;;;;;;;;;;;;;;;;;;without meta-data
(defn FadeInView [navigator]
  (r/create-class
   {:component-did-mount (fn [] (-> Animated
                                    (.timing
                                     (.-fadeAnim state) #js {:toValue 1 :duration 2000})
                                    (.start)))
    :reagent-render (fn [navigator]
                      [view
                       [animatedView
                        {:style {:width 250 :height 50 :background-color "powderblue" :opacity (.-fadeAnim state)}}
                        [text {:style {:font-size 28 :text-align "center" :margin 10}} "Fadingin"]]
                       [view  {:style {:flex-direction "column" :margin 40 :align-items "center"}}
                        [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                                              :on-press #(.resetTo navigator #js {:id "firstpage"})}
                         [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "press me"]]]])}))

(defn animation [{navigator :navigator}]
  [FadeInView navigator])
