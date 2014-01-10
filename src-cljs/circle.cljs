(ns hello.circle
  (:require [hello.math :as math]
            [hello.canvas :as canvas]))

(defn bounce' [x dx dt w]
  (let [x' (+ x (* dt dx))]
    (if (<= 0 x' w) [x' dx] [x (- dx)])))

(defn bounce [[x y dx dy] dt w h]
  (let [[x' dx'] (bounce' x dx dt w)
        [y' dy'] (bounce' y dy dt h)]
    [x' y' dx' dy']))

(defn advance [points dt w h]
  (for [point points]
    (bounce point dt w h)))

(defn render [context w h points]
  (doto context
    (canvas/set-fill-style! "#d3d7cf")
    (.fillRect 0 0 w h)
    (canvas/set-fill-style! "#75507b"))
  (let [[x y r] (math/fit-circle points)]
    (doto context
      (.beginPath)
      (canvas/draw-circle x y r)
      (.closePath)
      (.stroke)))
  (doseq [[x y _ _] points]
    (doto context
      (.beginPath)
      (canvas/draw-circle x y 5)
      (.closePath)
      (.fill))))

(defn make-random-point [w h]
  (let [x (rand w)
        y (rand h)
        t (rand (* 2 Math/PI))
        s 0.1
        dx (* s (.cos js/Math t))
        dy (* s (.sin js/Math t))]
    [x y dx dy]))

(defn make-random-points [n w h]
  (for [_ (range n)]
    (make-random-point w h)))

(defn run []
  (let [canvas (.querySelector js/document "canvas")
        w (.-width canvas)
        h (.-height canvas)
        context (.getContext canvas "2d")
        initial-state [0 (make-random-points 3 w h)]]
    (set! (.-strokeStyle context) "#2e3436")
    (canvas/animate
      initial-state
      (fn [t [last-t points]]
        (let [dt (- t last-t)
              points' (advance points dt w h)]
          (render context w h points)
          [t points'])))))
