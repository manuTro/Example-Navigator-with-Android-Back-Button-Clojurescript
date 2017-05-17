(ns future-app.android.animation
  (:require [reagent.core :as r :refer [atom create-class]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [future-app.events]
            [future-app.subs]))

(def ReactNative (js/require "react-native"))
(def ART (.-ART ReactNative))
(def Surface (r/adapt-react-class (.-Surface ART)))
(def Group (r/adapt-react-class (.-Group ART)))
(def Shape (r/adapt-react-class (.-Shape ART)))
; (def shape* (.createAnimatedComponent Animated Shape))

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
;(def state  #js {:fadeAnim fadeAnim})
(def state #js {})
(def radial-gradient (.-RadialGradient ART))
(def create-animated-component (.-createAnimatedComponent Animated))
(def Shape* (create-animated-component Shape) )


(defn interpolate
  [a-value easing]
  (.interpolate
    a-value
    #js {:inputRange (array 0 1)
         :outputRange (array 0 1)
         :easing easing}))

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
    :component-will-mount (fn [] (set! (.-fadeAnim state) fadeAnim))
    :reagent-render (fn [navigator]
                      [view
                       [animatedView
                        {:style {:width 250 :height 50 :background-color "powderblue" :opacity (.-fadeAnim state)}}
                        [text {:style {:font-size 28 :text-align "center" :margin 10}} "Fadingin"]]
                       [view  {:style {:flex-direction "column" :margin 40 :align-items "center"}}
                        [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                                              :on-press #(.resetTo navigator #js {:id "firstpage"})}
                         [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "press me"]]]
                       [view [Surface {:width 500 :height 500}
                              [Group {:x 100 :y 0}
                               [Shape {:d "M 25, 1.5, A 23.5, 23.5, 0, 1, 1, 24.99, 1.5 Z" :stroke "black" :strokeWidth 1}]]]]])}))

;  [view [Surface {:width 500 :height 500}
;         [Group {:x 100 :y 0}
;          [Shape {:d "M10 80 C 40 10, 65 10, 95 80 S 150 150, 180 80" :stroke "#2ca02c" :strokeWidth 1}]]]]


;read this to undestand the d property of SVG
;https://www.w3.org/TR/SVG/paths.html#PathDataCurveCommands
(defn animation [{navigator :navigator}]
  [FadeInView navigator])
