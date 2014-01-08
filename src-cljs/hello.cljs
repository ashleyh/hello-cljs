(ns hello)

(defn set-fill-style! [context style]
  (set! (.-fillStyle context) style))

(defn draw-circle [context x y r]
  (.arc context x y r 0 (* 2 Math/PI) true))

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

(defn animate [initial-state f]
  (.requestAnimationFrame js/window
    (fn [t]
      (let [next-state (f t initial-state)]
        (animate f next-state)))))

; i can't tell you how much i love typing this stuff out
(defn det22 [a b
             c d]
  (- (* a d) (* b c)))

(defn det33 [a b c
             d e f
             g h i]
  (let [d1 (det22 e f h i)
        d2 (det22 d f g i)
        d3 (det22 d e g h)]
    (+ (* a d1) (- (* b d2)) (* c d3))))

(defn sum-sq [x y]
  (+ (* x x) (* y y)))

(defn fit-circle [[p1 p2 p3]]
  (let [[x1 y1 _ _] p1
        [x2 y2 _ _] p2
        [x3 y3 _ _] p3
        r1 (sum-sq x1 y1)
        r2 (sum-sq x2 y2)
        r3 (sum-sq x3 y3)
        a (det33 x1 y1 1 x2 y2 1 x3 y3 1)
        d (det33 r1 y1 1 r2 y2 1 r3 y3 1)
        e (det33 r1 x1 1 r2 x2 1 r3 x3 1)
        f (det33 r1 x1 y1 r2 x2 y2 r3 x3 y3)
        x (/ d (* 2 a))
        y (/ e (* 2 a))
        r2 (+ (/ (sum-sq d e) (* 4 a a)) (/ f a))
        r (.sqrt js/Math r2)]
    [x (- y) r]))

(defn render [context w h points]
  (doto context
    (set-fill-style! "#d3d7cf")
    (.fillRect 0 0 w h)
    (set-fill-style! "#75507b"))
  (let [[x y r] (fit-circle points)]
    (doto context
      (.beginPath)
      (draw-circle x y r)
      (.closePath)
      (.stroke)))
  (doseq [[x y _ _] points]
    (doto context
      (.beginPath)
      (draw-circle x y 5)
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

(let [canvas (.querySelector js/document "canvas")
      w (.-width canvas)
      h (.-height canvas)
      context (.getContext canvas "2d")
      initial-state [0 (make-random-points 3 w h)]]
  (set! (.-strokeStyle context) "#2e3436")
  (animate
    initial-state
    (fn [t [last-t points]]
      (let [dt (- t last-t)
            points' (advance points dt w h)]
        (render context w h points)
        [t points']))))

