(ns recursion)

(defn product [coll]
  ;(apply * coll)
  (if (empty? coll)
    1
    (* (first coll) (product (rest coll)))))

(defn singleton? [coll]
  (and (not (empty? coll))
       (empty? (rest coll))))

(defn- empty-or-singleton? [coll]
  (or (empty? coll) (singleton? coll)))

(defn my-last [coll]
  (if (empty-or-singleton? coll)
    (first coll)
    (my-last (rest coll))))

(defn max-element [a-seq]
  (if (empty-or-singleton? a-seq)
    (first a-seq)
    (max (first a-seq) (max-element (rest a-seq)))))

(defn seq-max [seq-1 seq-2]
  (if (> (count seq-1) (count seq-2))
    seq-1
    seq-2))

(defn longest-sequence [a-seq]
  (if (empty-or-singleton? a-seq)
    (first a-seq)
    (seq-max (first a-seq) (longest-sequence (rest a-seq)))))

(defn my-filter [pred? a-seq]
  (let [value (first a-seq)]
    ; Let's be happy with just empty? now..
    (if (empty? a-seq)
      a-seq
      (if (pred? value)
        (cons value (my-filter pred? (rest a-seq)))
        (my-filter pred? (rest a-seq))))))

(defn sequence-contains? [elem a-seq]
  (cond (empty? a-seq) false
        (= elem (first a-seq)) true
        :else (sequence-contains? elem (rest a-seq))))

(defn my-take-while [pred? a-seq]
  (let [current (first a-seq)]
    (cond (empty? a-seq) a-seq
          (pred? current) (cons current (my-take-while pred? (rest a-seq)))
          :else '())))

(defn my-drop-while [pred? a-seq]
  (let [current (first a-seq)]
    (cond (empty? a-seq) a-seq
          (pred? current) (my-drop-while pred? (rest a-seq))
          :else a-seq)))

(defn seq= [a-seq b-seq]
  (cond (and (empty? a-seq) (empty? b-seq)) true                          ; all were equal
        (or (empty? a-seq) (empty? b-seq)) false                          ; only the other ran out
        (= (first a-seq) (first b-seq)) (seq= (rest a-seq) (rest b-seq))  ; current elements equal
        :else false))                                                     ; current elements not equal

(defn my-map [f seq-1 seq-2]
  (if (or (empty? seq-1) (empty? seq-2))
    '()
    (cons (f (first seq-1) (first seq-2))
          (my-map f (rest seq-1) (rest seq-2)))))

(defn power [n k]
  (if (< k 1) 1
              (* n (power n (dec k)))))

(defn fib "Horribly inefficient fibonacci generator unless HC compiler magic" [n]
  (cond (< n 1) 0
        (= n 1) 1
        :else   (+ (fib (- n 1)) (fib (- n 2)))))

(defn my-repeat [how-many-times what-to-repeat]
  (if (<= how-many-times 0) '()
                            (cons what-to-repeat (my-repeat (dec how-many-times) what-to-repeat))))

(defn my-range [up-to]
  (let [to-append (dec up-to)]
    (if (< to-append 0) '()
                        (conj (my-range to-append) to-append))))

(defn tails [a-seq]
  (if (empty? a-seq)
    ; Mildly inelegant feel to this. Guidance mentioned map/reverse..?
    '([])
    (conj (tails (rest a-seq)) a-seq)))

(defn inits [a-seq]
  ; But then, map and reverse are useful at least here.
  (map reverse (tails (reverse a-seq))))

(defn rotations [a-seq]
  ; Makes little sense to have () yield (()). It even breaks the formula of
  ; (count a-seq) == (count rotations). Perhaps there is a beter way..
  (if (empty? a-seq)
    '([])
    (rest (map concat (tails a-seq) (reverse (inits a-seq))))))

(defn my-frequencies-helper [freqs a-seq]
  (let [current (first a-seq)]
    (if (empty? a-seq)
      freqs
      (my-frequencies-helper (assoc freqs current (inc (get freqs current 0)))
                             (rest a-seq)))))

(defn my-frequencies [a-seq]
  (my-frequencies-helper {} a-seq))

(defn un-frequencies [a-map]
  (let [key-and-value (first a-map)
        current-key (first key-and-value)
        current-value (second key-and-value)]
    (if (empty? a-map)
      '()
      (concat (repeat current-value current-key) (un-frequencies (rest a-map))))))

(defn my-take [n coll]
  (if (or (empty? coll) (<= n 0))
    '()
    (cons (first coll) (my-take (dec n) (rest coll)))))

(defn my-drop [n coll]
  (cond
    (empty? coll) '()
    (<= n 0) coll
    :else (my-drop (dec n) (rest coll))))

(defn halve [a-seq]
  (let [pivot (int (/ (count a-seq) 2))]
    (vector (my-take pivot a-seq) (my-drop pivot a-seq))))

(defn seq-merge [a-seq b-seq]
  (let [a (first a-seq)
        b (first b-seq)
        a-empty (empty? a-seq)
        b-empty (empty? b-seq)
        pick-a (fn [] (cons a (seq-merge (rest a-seq) b-seq)))
        pick-b (fn [] (cons b (seq-merge a-seq (rest b-seq))))]
    (cond
      (and a-empty b-empty) '()
      (and b-empty (not a-empty)) (pick-a)
      (and a-empty (not b-empty)) (pick-b)
      (<= a b) (pick-a)
      :else (pick-b))))

(defn merge-sort [a-seq]
  (if (empty-or-singleton? a-seq)
    a-seq
    (apply seq-merge (map merge-sort (halve a-seq)))))

(defn split-into-monotonics [a-seq]
; NOTE Sorting is possibly done in a mutable way
  [:-])

(defn permutations [a-set]
  [:-])

(defn powerset [a-set]
  (loop [values a-set
         powerset #{#{}}]
    (let [value (first values)]
      (if (empty? values)
        powerset
        (recur (rest values) (apply conj
                               powerset
                               (map (fn [a-set] (conj a-set value))
                                         powerset)))))))
