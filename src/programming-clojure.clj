(def visitors (atom #{}))

(defn hello [username]
    (swap! visitors conj username)
    (str "Hello " username))

; user=> (hello "Bob")
; "Hello Bob"
; user=> @visitors
; #{"Bob"}
; user=> (hello "Plop")
; "Hello Plop"
; user=> (deref visitors)
; #{"Plop" "Bob"}

(require 'my) ; load module
(refer 'my)   ; inject module stuff in this namespace
(use :reload 'my) ; require+refer (reload for REPL)

(doc str) ; (doc reduce)
(find-doc "reduce")
