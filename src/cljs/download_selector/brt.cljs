(ns download-selector.brt
  (:require [download-selector.sheets :as sheets]
            [download-selector.styles :as styles]
            [cemerick.url :as url]
            [reagent.core :as reagent]
            [ol.extent :as ext]
            [ol.control :as ctrl]
            [ol.interaction.Select]
            [ol]
            [ol.proj :as proj])
  (:import [ol Map View]
           [ol.control ZoomToExtent]
           [ol.interaction.Interaction]
           [ol.layer Tile]
           [ol.layer]
           [ol.proj Projection]
           [ol.source WMTS OSM TileDebug]))

(enable-console-print!)
(js* "proj4.defs(\"EPSG:28992\", \"+proj=sterea +lat_0=52.15616055555555 +lon_0=5.38763888888889 +k=0.9999079 +x_0=155000 +y_0=463000 +ellps=bessel +units=m +no_defs\");")

(def selected (reagent/atom nil))

(def projection "EPSG:28992")
(def matrixset-name "EPSG:28992")
(def projection-extent (clj->js [-285401.92, 22598.08, 595401.9199999999, 903401.9199999999]))
(def size (/ (ext/getWidth projection-extent) 256))
(def view-projection (Projection. (clj->js {:code "EPSG:28992" :unit "m" :extent projection-extent})))
(def zoom-to-extent-control (ol.control.ZoomToExtent. (clj->js {:extent projection-extent :label "E"})))


(def selector-view (View. (clj->js {:projection view-projection
                                    :center     (ext/getCenter projection-extent)
                                    :zoom       2.5
                                    :minZoom    2.5
                                    :maxZoom    11.5})))

(defn exp [x n]
  (reduce * (repeat n x)))

(def matrix-ids (clj->js (into [] (map #(str projection ":" %) (range 14)))))
(def resolutions (clj->js (into [] (map #(/ size (exp 2 %)) (range 14)))))

(def background (ol.layer.Tile. (clj->js {:source (ol.source.WMTS.
                                                    (clj->js {:url        "//geodata.nationaalgeoregister.nl/tiles/service/wmts/brtachtergrondkaart"
                                                              :layer      "brtachtergrondkaart"
                                                              :matrixSet  matrixset-name
                                                              :format     "image/png"
                                                              :projection projection
                                                              :tileGrid   (ol.tilegrid.WMTS.
                                                                            (clj->js {:origin      (ext/getTopLeft projection-extent)
                                                                                      :resolutions resolutions
                                                                                      :matrixIds   matrix-ids}))
                                                              }))
                                          })))

(def selector-features (sheets/create-features sheets/brt-rd-sheets))

(def selector-source (ol.source.Vector. (clj->js {:features selector-features})))
(def selector-layer (ol.layer.Vector. (clj->js {:source           selector-source
                                                :visible          true
                                                :visibleZoomLevel 1
                                                :style            (styles/less-more styles/default-style-no-label styles/default-style selector-view 3.0)})))

(def selector-map
  (with-meta (fn [] [:div#map])
             {:component-did-mount
              (fn [this]
                (def map1 (ol.Map. (clj->js {
                                             :logo     false
                                             :layers   [background, selector-layer]
                                             :target   "map"
                                             :view     selector-view
                                             :controls (ctrl/defaults (clj->js {:attributeOptions {:collapsible false}}))
                                             })))

                (def selector (ol.interaction.Select. (clj->js {:layers [selector-layer]
                                                                :style  styles/highlight-style
                                                                })))

                (-> selector (.getFeatures)
                    (.on "change:length"
                         (fn [e]
                           (let [features (.-target e)]
                             (if (= 1 (.getLength features))
                               (reset! selected (-> features (.item 0) (.get "name")))
                               (reset! selected nil))))))

                (.addInteraction map1 selector)
                (.addControl map1 zoom-to-extent-control)
                )}))

(def base (get (:query (url/url (-> js/window .-location .-href))) "base" ""))

(defn make-link [sheet]
  "Replaces $SHEET with sheet"
  (clojure.string/replace base "$SHEET" sheet))

(defn download-link [selected]
  (when @selected
    [:a {:href (make-link @selected)} "Download"]))

(defn app []
   [:div
    [selector-map]
    [:table
     [:tr [:td "KAARTBLAD"] [:td @selected]]
     [:tr [:td "DOWNLOAD"] [:td (download-link selected)]]]])

(defn ^:export run []
  (reagent/render [app] (.getElementById js/document "app")))
