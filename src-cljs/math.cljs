(ns hello.math)

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
