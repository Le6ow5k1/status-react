(ns status-im.ui.screens.wallet.request.views
  (:require-macros [status-im.utils.views :as views])
  (:require
    [re-frame.core :as re-frame]
    [status-im.components.react :as react]
    [status-im.components.qr-code :as components.qr-code]
    [status-im.components.toolbar-new.actions :as actions]
    [status-im.components.toolbar-new.view :as toolbar]
    [status-im.components.status-bar :as status-bar]
    [status-im.ui.screens.wallet.styles :as wallet.styles]
    [status-im.components.icons.vector-icons :as vi]
    [status-im.ui.screens.wallet.components.views :as components]
    [status-im.ui.screens.wallet.request.styles :as styles]
    [status-im.components.styles :as components.styles]
    [status-im.i18n :as i18n]
    [status-im.utils.platform :as platform]))

(defn toolbar-view []
  [toolbar/toolbar2 {:style wallet.styles/toolbar :hide-border? true}
   [toolbar/nav-button (actions/back-white actions/default-handler)]
   [toolbar/content-title {:color :white} (i18n/label :t/request-transaction)]])

(defn send-request []
  (re-frame/dispatch [:navigate-to-modal
                      :contact-list-modal
                      {:handler #(re-frame/dispatch [:wallet-send-request %1])
                       :action  :request
                       :params  {:hide-actions? true}}]))

(views/defview qr-code []
  (views/letsubs [account [:get-current-account]]
    [components.qr-code/qr-code
     {:value   (.stringify js/JSON (clj->js {:address (:address account)
                                             :amount  0}))
      :bgColor :white
      :fgColor "#4360df"
      :size    256}]))

(views/defview request-transaction []
  ;;Because input field is in the end of view we will scroll to the end on input focus event
  (views/letsubs [amount-error [:get-in [:wallet/request-transaction :amount-error]]
                  request-enabled? [:wallet.request/request-enabled?]
                  scroll (atom nil)]
    [react/keyboard-avoiding-view wallet.styles/wallet-modal-container
     [status-bar/status-bar {:type :wallet}]
     [toolbar-view]
     [react/scroll-view {:ref #(reset! scroll %)}
      [react/view components.styles/flex
        [react/view styles/network-container
         ;;TODO (andrey) name of active network should be used
         [components/network-label styles/network-label "Testnet"]
         [react/view styles/qr-container
          [qr-code]]]
        [react/view wallet.styles/choose-wallet-container
         [components/choose-wallet]]
        [react/view wallet.styles/amount-container
         [components/amount-input
          {:error         amount-error
           :input-options {:on-focus (fn [] (when @scroll (js/setTimeout #(.scrollToEnd @scroll) 100)))
                           :on-change-text
                           #(do (re-frame/dispatch [:set-in [:wallet/request-transaction :amount] %])
                                (re-frame/dispatch [:wallet-validate-request-amount]))}}]
         [react/view wallet.styles/choose-currency-container
          [components/choose-currency wallet.styles/choose-currency]]]]]
     [components/separator]
     [react/view wallet.styles/buttons-container
      [react/touchable-highlight {:on-press #()}
       [react/view (wallet.styles/button-container false)
        [vi/icon :icons/share {:color :white :container-style styles/share-icon-container}]
        [components/button-text (i18n/label :t/share)]]]
      [react/view components.styles/flex]
      [react/touchable-highlight {:on-press (when request-enabled? send-request)}
       [react/view (wallet.styles/button-container request-enabled?)
        [components/button-text (i18n/label :t/send-request)]
        [vi/icon :icons/forward {:color :white :container-style wallet.styles/forward-icon-container}]]]]]))