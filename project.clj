(defproject hello-cljs "1"
  :source-paths ["src-clj"]
  :plugins [[lein-cljsbuild "1.0.1"]]
  :dependencies [[org.clojure/clojurescript "0.0-2014"]]
  :cljsbuild {
              :builds {
                       :debug {}
                       :opt {:compiler {:optimizations :advanced
                                        :pretty-print false}}}})
