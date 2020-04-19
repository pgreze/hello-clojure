(ns oneh
  (:require [clojure.pprint :as pp])
  (:require [clojure.string :as str])
  (:gen-class))

(def aString "hello1 world")

(defn formats
  []
  [(format "this is a string %s" aString)
   (format "5 spaces and %5d" 20)
   (format "Leading zeros %04d" 16)
   (format " %-4d Left justified" 16)
   (str/split aString #" ")
   (str/split aString #"\d")
   (str/join " " ["The" "Big" "Cheese"])
   (list* 1 2 [3 4])
   (cons 3 [4 5])
   (hash-map "k1" "v1" "k2" "v2")])

(defn atomExample
  [x]
  (def atomEx (atom x))
  (add-watch atomEx :watcher
             (fn [key atom old-state new-state]
               (println "atomEx changed from "
                        old-state "to" new-state)))
  (println "1)" @atomEx)
  (reset! atomEx 10)
  (println "2)" @atomEx)
  (swap! atomEx inc)
  (println "Inc" @atomEx))

(defn agentExample
  []
  (def tickets-sold (agent 0))

  (send tickets-sold + 15)
  (println)   ; Wait for value update...
  (println "tickets sold:" @tickets-sold)

  (send tickets-sold + 10)
  (await-for 100 tickets-sold)
  (println "tickets" @tickets-sold)

  (shutdown-agents))

(defn polymorphicFunc
  ([x y z]
   [+ x y z])
  ([x y]
   [+ x y]))

(defn hello-all
  [& names]
  (map (defn greet [name] (println "Hello" name)) names))
; (oneh/hello-all :Louis :Georges)

(defn loops
  ([x]
   (def i (atom 1))
   (while (<= @i x)
     (do
       (println @i)
       (swap! i inc))))
  ([x y]
   (dotimes [i x]
     (println (* i y))))
  ([x y z]
   (loop [i x]
     (when (< i y)
       (println (* i z))
       (recur (+ i 1))
       )))
  ([]
   (doseq [x '[1 2 3 4 5]]
     (println x))))

(defn -main
  "Main function"
  [& args]
  (pp/pprint (formats)))
