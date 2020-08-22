(def visitors (atom #{}))

(defn hello [username]
    (swap! visitors conj username)
    (str "Hello " username))

(hello "Bob")
; "Hello Bob"
@visitors
; #{"Bob"}
(hello "Plop")
; "Hello Plop"
(deref visitors)
; #{"Plop" "Bob"}

'(require 'my) ; load module
'(refer 'my)   ; inject module stuff in this namespace
'(use :reload 'my) ; require+refer (reload for REPL)

'(doc str) ; (doc reduce)
'(find-doc "reduce")

;
; Maps, Keywords, Records
;

; Optional ,
(def inventors {"Lisp" "McCarthy", "Clojure" "Hickey"})
; Maps are functions
(inventors "Lisp")
(inventors "Foo")
; nil
(get inventors "Foo" "Unknown")
; "Unknown"

; Keywords start with a :
:foo
(def inventors {:Lisp "McCarthy", :Clojure "Hickey"})
; Request a map by using a keyword request
(:Lisp inventors)
; "McCarthy"

; record == maps with fixed keys (= data class)
(defrecord Book [title author])
(def b (->Book "My Life" "Me"))
; => #user.Book{:title "My Life", :author "Me"}
(:title b)
; => "My Life"
(Book. "Hello" "World")
; Alternative syntax

;
; Reader macros
;
; Popular ones:
; ; for a comment
; '(1 2) prevents evaluation
; @form -> (deref form)
;
; About why users cannot create their own reader macros:
; https://tinyurl.com/clojure-reader-macros
; > they come at a price in lack of interoperability, much more so than
; > ordinary macros, which can be placed in namespaces
;

;
; Functions
;
; = a list whose first elements resolves to a function
(str "Hello" " " "World")
; Usually hyphenated, as in clear-agent-errors
; if a predicate -> end with ?
(string? "Hello")
(keyword? :word)
(symbol? 'hello)
; Define your own with:
'(defn name doc-string? attr-map? [params*] body)
(defn hellon
    ([] (hello "You"))
    ([name] (str "Hello " name)))
; Variable arity = nargs
(defn party [name & friends]
    (str "Hello " name " and your " (count friends) " friend"))
(party "Me" "Lucas" "Georges")

;
; Anonymous Functions
;
'(fn [params*] body)
(require '[clojure.string :as str])
(filter (fn [w] (> (count w) 2)) (str/split "A fine day" #"\W+"))
; Alternative syntax with #(body) and args like %1 %2
(filter #(> (count %) 2) (str/split "A fine day" #"\W+"))
; let allows to name a function only inside the provided block
(let [indexable-word? #(> (count %) 2)]
    (filter indexable-word? (str/split "A fine day" #"\W+")))

;
; Vars, Bindings and Namespaces
;
; Vars can be aliased, have metadata, doc, type hints, etc.
(def foo 10)
; => #'user/foo
foo
; => 10
(var foo)
; => #'user/foo
#'foo
; => #'user/foo
; Destructuring is allowing to bind in args
(defn greet-author-1 [author]
    (str "Hello " (:first-name author)))
(greet-author-1 {:last-name "Lucas" :first-name "Georges"})
(defn greet-author-2 [{fname :first-name}]
    (str "Hello " fname))
(greet-author-2 {:last-name "Lucas" :first-name "Georges"})
; Another example
(let [[x y] [1 2 3]] [x y])
; => [1 2]
(let [[_ _ z] [1 2 3]] z)
; => 3
; Bind both a collection and elements with :as
(let [[x y :as coords] [1 2 3 4 5 6]]
    (str "x: " x ", y: " y ", total dimensions " (count coords)))
; => "x: 1, y: 2, total dimensions 6"
(defn ellipsize [words]
    (let [[w1 w2 w3] (str/split words #"\s+")]
        (str/join " " [w1 w2 w3 "..."])))
(ellipsize "Mon vieux theatre me jouait souvent une nouvelle piece")
; => "Mon vieux theatre ..."
;
; Namespaces
(def x 1)
; => #'user/x
(resolve 'x)
; => #'user/x
(in-ns 'myapp)
; => #object[clojure.lang.Namespace 0x66049124 "myapp"]
; When creating a new namespace, java.lang is always available
String
; => java.lang.String
; BUT not clojure.core
reduce
; Unable to resolve symbol: reduce in this context
(clojure.core/use 'clojure.core)
; => nil
reduce
; => #object[clojure.core$reduce 0x9031c7c "clojure.core$reduce@9031c7c"]
java.io.File/separator
; => "/"
; import is available for Java classes
(import '(java.io InputStream File))
(.exists (File. "/tmp"))
; By convention, it's common to use (ns ...) with require/import/use
'(ns my.namespace
    [:require [clojure.string :as str]]
    [:import (java.io.File)])
; Return to the default REPL namespace
(in-ns 'user)
