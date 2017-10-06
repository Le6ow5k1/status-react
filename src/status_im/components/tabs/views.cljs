(ns status-im.components.tabs.views
  (:require [re-frame.core :as re-frame]
            [status-im.components.react :as react]
            [status-im.utils.platform :as platform])
  (:require-macros [status-im.utils.views :refer [defview]]))

(defview tab [index content tab-style on-press active?]
  [react/view (when tab-style
                (tab-style active?))
   [react/touchable-highlight {:disabled active?
                               :on-press #(on-press index)}
    [react/view
     [content active?]]]])

(defn tabs [tabs-container-style indexed-tabs current-tab tab-style on-press]
  [react/view tabs-container-style
   (for [[index {:keys [content view-id]}] indexed-tabs]
     ^{:key index} [tab index content tab-style on-press (= view-id current-tab)])])

(defn swipable-tabs [current-tab {:keys [tabs-list bottom-tabs? main-container-style tabs-container-style tab-style]}]
  (let [swiper        (atom nil)
        indexed-tabs  (map-indexed vector tabs-list)
        tab->index    (reduce (fn [acc [index tab]]
                                (assoc acc (:view-id tab) index))
                              {}
                              indexed-tabs)
        index->tab    (clojure.set/map-invert tab->index)
        get-tab-index #(get tab->index % 0)]
    (fn [current-tab]
      (let [current-tab-index (get-tab-index current-tab)
            on-press          (fn [index]
                                (.scrollBy @swiper (- index current-tab-index)))]
        [react/view main-container-style
         (when-not bottom-tabs?
           [tabs tabs-container-style indexed-tabs current-tab tab-style on-press])
         [react/swiper {:loop             false
                        :shows-pagination false
                        :index            current-tab-index
                        :ref              #(reset! swiper %)
                        :on-index-changed #(re-frame/dispatch [:change-tab (index->tab %)])}
          (for [[index {:keys [screen view-id]}] indexed-tabs]
            ^{:key index} [screen (= (get-tab-index view-id) current-tab-index)])]
         (when bottom-tabs?
           [tabs tabs-container-style indexed-tabs current-tab tab-style on-press])]))))
