(ns status-im.components.tabs.styles
  (:require-macros [status-im.utils.styles :refer [defnstyle defstyle]])
  (:require [status-im.components.styles :as styles]
            [status-im.utils.platform :as platform]))


(def tabs-height (if platform/ios? 52 56))
(def tab-height (dec tabs-height))

(defn tabs-container [hidden?]
  {:flex-direction :row

   :height           tabs-height
   :background-color styles/color-white
   :transform        [{:translateY 1}]})

(defnstyle tab [active?]
  {:flex                1
   :height              tab-height
   :justify-content     :center
   :align-items         :center
   :border-bottom-width (if active? 2 1)
   :border-bottom-color (if active?
                          styles/color-blue4
                          styles/color-gray10-transparent)})


(def tabs-container-line
  {:border-top-width 1
   :border-top-color styles/color-light-gray3})

(def tabs-inner-container
  {:flexDirection   :row
   :height          tab-height
   :opacity         1
   :justifyContent  :center
   :alignItems      :center})


(defnstyle tab-title [active?]
  {:ios        {:font-size 11}
   :android    {:font-size 12}
   :text-align :center
   :color      (if active?
                 styles/color-blue4
                 styles/color-gray4)})

(defn tab-icon [active?]
  {:color (if active? styles/color-blue4 styles/color-gray4)})

(def tab-container
  {:flex                1
   :height              tab-height
   :justify-content     :center
   :align-items         :center})

(def swiper
  {:shows-pagination false})

(defn main-swiper [tabs-hidden?]
  (merge
   swiper
   {}))

(def main-container
  {:flex             1
   :background-color styles/color-white
   })
