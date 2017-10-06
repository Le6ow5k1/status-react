(ns ^{:doc    "todo"
      :author "Goran Jovic"}
status-im.ui.screens.discover.dapp-details.views
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require
    [status-im.components.react :as react]
    [status-im.ui.screens.discover.components.views :as components]
    [status-im.components.chat-icon.screen :as chat-icon.screen]
    [status-im.components.common.common :as common]
    [status-im.components.icons.vector-icons :as vector-icons]
    [status-im.components.toolbar-new.view :as toolbar]
    [status-im.ui.screens.discover.styles :as styles]))

(defn section [title content]
  [react/view {:background-color :white}
   [react/view {:background-color :white
                :padding 10}
    [react/text {:font :small
                 :style {:color :lightgray}} title]]
   [react/view {:background-color :white
                :padding-left 10
                :padding-right 10}
    [react/text {:font :medium} content]]])

(def mock-dapp
  {:name        "TokenBot"
   :description "Based on Ethereum - The next generation blockchain network. Speculate on anything with an easy-to-use prediction market."
   :url         "https://app.gnosis.net/"})

(defview dapp-details []
  (letsubs [current-account [:get-current-account]]         ;
    [react/view styles/discover-container
     [toolbar/toolbar2 {}
      toolbar/default-nav-back
      [react/view]]
     [react/view {:flex-direction   :column
                  :background-color :lightgray}
      [react/view {:flex-direction   :row
                   :justify-content  :flex-start
                   :align-items      :center
                   :height           80
                   :background-color :white}
       [react/view {:flex             0.15
                    :background-color :white
                    :padding-left 10}
        [chat-icon.screen/chat-icon "contacts://commiteth" {:size 50}]]
       [react/view {:flex             0.85
                    :background-color :white}
        [react/text {:font :medium
                     :style {:margin-left 10}} (:name mock-dapp)]]]
      [react/view {:flex-direction :row
                   :margin-top     15
                   :margin-bottom  15}
       [react/view {:height           80
                    :flex             0.15
                    :justify-content  :center
                    :align-items      :center
                    :background-color :white}
        [vector-icons/icon :icons/open {:color :active}]]
       [react/view {:height           80
                    :flex             0.85
                    :background-color :white
                    :flex-direction   :row
                    :justify-content  :flex-start
                    :align-items      :center}
        [react/text {:font :medium
                     :style {:margin-left 10
                             :color :blue}} "Open"]]]
      [section "Description" (:description mock-dapp)]
      [common/separator]
      [section "URL" (:url mock-dapp)]]]))