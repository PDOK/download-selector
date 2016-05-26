(def version (slurp "VERSION"))
(def git-ref (clojure.string/replace (:out (clojure.java.shell/sh "git" "rev-parse" "HEAD"))#"\n" "" ))

(spit "resources/GITREF" git-ref)

(defproject nl.pdok/download-selector version
  :description "PDOK selector for extract downloads"
  :url "http://www.pdok.nl"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [com.cemerick/url "0.1.1"]
                 [cljsjs/jquery "2.2.2-0"]
                 [cljsjs/openlayers "3.15.1"]
                 [reagent "0.5.1"
                  :exclusions [org.clojure/tools.reader]]
                 [reagent-forms "0.5.23"]
                 [reagent-utils "0.1.8"]
                 [hiccup "1.0.5"]
                 [secretary "1.2.3"]
                 [venantius/accountant "0.1.7"
                  :exclusions [org.clojure/tools.reader]]]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-figwheel "0.5.3-1"
             :exclusions [org.clojure/core.memoize
                          ring/ring-core
                          org.clojure/clojure
                          org.ow2.asm/asm-all
                          org.clojure/data.priority-map
                          org.clojure/tools.reader
                          org.clojure/clojurescript
                          org.clojure/core.async
                          org.clojure/tools.analyzer.jvm]]
             [lein-asset-minifier "0.3.0"
              :exclusions [org.clojure/clojure]]]


  :min-lein-version "2.5.0"
  :clean-targets ^{:protect false} ["resources/public/out" "resources/public/release"]
  :source-paths ["src/clj"]
  :target-path "target"

  :minify-assets {:assets  {"resources/public/release/app.css" ["resources/public/css/ol.css" "resources/public/css/site.css"]}
                  :options {:optimization :advanced}}

  :profiles {:dev {:hooks [leiningen.cljsbuild]
                   :cljsbuild {:builds {:dev {:source-paths ["src/cljs"]
                                              :figwheel {:on-jsload "download-selector.brt/run"}
                                              :compiler     {:main                      "download-selector.brt"
                                                             :source-map                true
                                                             :output-to                 "resources/public/out/app.js"
                                                             :output-dir                "resources/public/out"
                                                             :asset-path                "/out"
                                                             :optimizations             :none
                                                             :pretty-print              true
                                                             :closure-extra-annotations #{"api" "observable"}
                                                             :closure-defines           {"goog.DEBUG"        false
                                                                                         "ol.ENABLE_DOM"     true
                                                                                         "ol.ENABLE_VECTOR"  true
                                                                                         "ol.ENABLE_PROJ4JS" true
                                                                                         "ol.ENABLE_WEBGL"   true}}}}

                                  }}
             :release {:hooks [minify-assets.plugin/hooks leiningen.cljsbuild]
                       :cljsbuild {:builds {:release {:source-paths ["src/cljs"]
                                                      :figwheel {:on-jsload "download-selector.brt/run"}
                                                      :compiler     {:main "download-selector.brt"
                                                                     :output-to                 "resources/public/release/app.js"
                                                                     :output-dir                "resources/public/release"
                                                                     :asset-path                "/release"
                                                                     :optimizations             :advanced
                                                                     :pretty-print              false
                                                                     :closure-extra-annotations #{"api" "observable"}
                                                                     :closure-defines           {"goog.DEBUG"        false
                                                                                                 "ol.ENABLE_DOM"     true
                                                                                                 "ol.ENABLE_VECTOR"  true
                                                                                                 "ol.ENABLE_PROJ4JS" true
                                                                                                 "ol.ENABLE_WEBGL"  true}}}}}}})
