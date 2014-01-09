(ns hello.canvas)

(defn set-fill-style! [context style]
  (set! (.-fillStyle context) style))

(defn draw-circle [context x y r]
  (.arc context x y r 0 (* 2 Math/PI) true))

(defn animate [initial-state f]
  (.requestAnimationFrame js/window
    (fn [t]
      (let [next-state (f t initial-state)]
        (animate f next-state)))))
