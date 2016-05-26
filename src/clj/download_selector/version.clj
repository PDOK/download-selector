 (ns download-selector.version
     (:require clojure.java.io))

 (defmacro defgitref []
           (slurp (clojure.java.io/resource "GITREF")))