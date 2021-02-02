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

(define list1 (cons 7 empty)) ; 1elementige Liste: 7
(define list2 (cons 3 (cons 7 empty))) ; 2elementige Liste: 3 7
(define list3 (cons 12 (cons 3 (cons 7 empty)))) ; 3elementige Liste: 12 3 7
(define list4 (cons 5 list3)) ; 4elementige Liste: 5 12 3 7

; Elemente einer Liste aufsummieren
(: list-sum (list-of-numbers -> number))

(check-expect (list-sum list4) 27)

(define list-sum
  (lambda (list)
    (cond
      ((empty? list) 0) ; neutrales Element bezüglich +
      ((cons? list) (+ (first list)
                       (list-sum (rest list)))))))

(define list-product
  (lambda (list)
    (cond
      ((empty? list) 1) ; neutrales Element bezüglich *
      ((cons? list) (* (first list)
                       (list-product (rest list)))))))

; Alle geraden Elemente einer Liste extrahieren
(: extract-evens (list-of-numbers -> list-of-numbers))

(check-expect (extract-evens (cons 1 (cons 4 (cons 7 (cons 10 (cons 12 empty))))))
              (cons 4 (cons 10 (cons 12 empty))))

(define extract-evens
  (lambda (list)
    (cond
      ((empty? list) list)
      ((cons? list) (if (even? (first list))
                        (cons (first list) (extract-evens (rest list)))
                        (extract-evens (rest list)))))))

(define extract-positives
  (lambda (list)
    (cond
      ((empty? list) list)
      ((cons? list) (if (positive? (first list))
                        (cons (first list) (extract-evens (rest list)))
                        (extract-positives (rest list)))))))

; 2 Listen aneinanderhängen
(: concat (list-of-numbers list-of-numbers -> list-of-numbers))
