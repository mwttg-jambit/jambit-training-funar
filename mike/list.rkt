;; Die ersten drei Zeilen dieser Datei wurden von DrRacket eingefügt. Sie enthalten Metadaten
;; über die Sprachebene dieser Datei in einer Form, die DrRacket verarbeiten kann.
#reader(lib "beginner-reader.rkt" "deinprogramm" "sdp")((modname list) (read-case-sensitive #f) (teachpacks ()) (deinprogramm-settings #(#f write repeating-decimal #f #t none explicit #f ())))
; Eine Liste (von Zahlen) ist eins der folgenden:
; - die leere Liste
; - eine Cons-Liste, bestehend aus erstem Element und Rest-Liste
;                                                          ^^^^^ Selbstbezug
(define list-of-numbers
  (signature (mixed empty-list cons-list)))

(define-record empty-list
  make-empty-list
  empty?)

(define empty (make-empty-list))

; Eine Cons-Liste besteht aus:
; - erstes Element
; - Rest-Liste
(define-record cons-list
  cons
  cons?
  (first number)
  (rest list-of-numbers))
