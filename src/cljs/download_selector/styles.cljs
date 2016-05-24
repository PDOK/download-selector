(ns download-selector.styles
  (:import ol.style.Text))

;; custom style (per feature) has one param (resolution) and feature as this

(defn default-style [feature resolution]
  (let [label (.get feature "name")]
    (ol.style.Style. (clj->js {:stroke (ol.style.Stroke. (clj->js {:color "#3399CC"
                                                                   :width 1.25}))
                               :fill   (ol.style.Fill. (clj->js {:color "rgba(255,255,255,0.3)"}))
                               :text   (ol.style.Text. (clj->js {:text   label
                                                                 :fill   (ol.style.Fill. (clj->js {:color "white"}))
                                                                 :stroke (ol.style.Stroke. (clj->js {:color "#3399CC"
                                                                                                     :width 3}))}))}))))
(defn default-style-no-label [feature resolution]
  (let [label (.get feature "name")]
    (ol.style.Style. (clj->js {:stroke (ol.style.Stroke. (clj->js {:color "#3399CC"
                                                                   :width 1.25}))
                               :fill   (ol.style.Fill. (clj->js {:color "rgba(255,255,255,0.3)"}))}))))

(defn less-more [less-style more-style view from-zoom]
  (fn [feature resolution]
    (let [zoom (.getZoom view)]
      (if (< zoom from-zoom)
        (less-style feature resolution)
        (more-style feature resolution)))))

(defn highlight-style [feature resolution]
  (let [label (.get feature "name")]
    (ol.style.Style. (clj->js {:stroke (ol.style.Stroke. (clj->js {:color "rgb(209,111,20)"
                                                                   :width 2}))
                               :fill   (ol.style.Fill. (clj->js {:color "rgba(255,255,255,0)"}))
                               :text   (ol.style.Text. (clj->js {:text   label
                                                                 :fill   (ol.style.Fill. (clj->js {:color "black"}))
                                                                 :stroke (ol.style.Stroke. (clj->js {:color "#3399CC"
                                                                                                     :width 3}))}))}))))