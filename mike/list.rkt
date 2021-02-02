;; Die ersten drei Zeilen dieser Datei wurden von DrRacket eingefügt. Sie enthalten Metadaten
;; über die Sprachebene dieser Datei in einer Form, die DrRacket verarbeiten kann.
#reader(lib "vanilla-reader.rkt" "deinprogramm" "sdp")((modname list) (read-case-sensitive #f) (teachpacks ()) (deinprogramm-settings #(#f write repeating-decimal #f #t none explicit #f ())))
; Eine Liste (von Zahlen) ist eins der folgenden:
; - die leere Liste
; - eine Cons-Liste, bestehend aus erstem Element und Rest-Liste
;                                                          ^^^^^ Selbstbezug
; Signaturkonstruktor
#|
(: list-of (signature -> signature))

(define list-of
  (lambda (element)
    (signature (mixed empty-list
                      (cons-list-of element)))))

(define-record empty-list
  make-empty-list
  empty?)

(define empty (make-empty-list))

; Eine Cons-Liste besteht aus:
; - erstes Element
; - Rest-Liste
(define-record (cons-list-of element) ; sorgt dafür, daß define-record ein lambda erzeugt
  cons
  cons?
  (first element)
  (rest (list-of element)))
|#

; Bug:
;(: list-of-numbers signature)
(define list-of-numbers (signature (list-of number)))

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

(: list-fold (%b (%a %b -> %b) (list-of %a) -> %b))

(check-expect (list-fold 0 + (list 1 2 3 4)) 10)

(define list-fold
  (lambda (for-empty for-cons list)
    (cond
      ((empty? list) for-empty)
      ((cons? list) (for-cons (first list)
                              (list-fold for-empty for-cons (rest list)))))))

; Halbgruppe:
; Mathematik:
; Menge M
; op : M x M -> M
; Assoziativgesetz:
; (a op b) op c = a op (b op c)

; Halbgruppe + neutrales Element = Monoid
; n : M
; für alle x \in M : x op n = n op x = x

; beim Programmieren:
; Typ / Signatur M
; (: op (M M -> M))
; (op (op a b) c) = (op a (op b c))

; Kommutativgesetzt:
; a op b = b op a


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

#;(define extract-positives
  (lambda (list)
    (cond
      ((empty? list) list)
      ((cons? list) (if (positive? (first list))
                        (cons (first list) (extract-evens (rest list)))
                        (extract-positives (rest list)))))))

; Abstraktion
; 1. Schritt: kopieren
; 2. Schritt: umbenennen (rekursive Aufrufe nicht vergessen!)
; 3. Schritt: Unterschiede durch Variablen ersetzen
; 4. Schritt: "Variablen in Lambda unterbringen" (neues Lambda oder altes erweitern)
;             (rekursive Aufrufe nicht vergessen)

; Elemente aus Liste extrahieren
; (: positive? (number -> boolean))
; (: even? (number -> boolean))

(: extract ((%element -> boolean) (list-of %element) -> (list-of %element)))

#|
(extract even? (list "Mike"))
(: even? (number -> boolean))
NICHT:
(: even? (any -> boolean))

=>

%element = number

(: (list "Mike") (list-of number)) NEIN!

any Obermenge number

(A -> C)
(B -> C)

A < B
=>
(A -> C) > (B -> C)

Funktionssignaturen sind KONTRAVARIANT in der Argumentsignatur

A -> C KANN WENIGER als B -> C
A -> C ist SCHWÄCHER als B -> C

|#

(check-expect (extract even? (cons 1 (cons 4 (cons 7 (cons 10 (cons 12 empty))))))
              (cons 4 (cons 10 (cons 12 empty))))
(check-expect (extract positive? (cons 1 (cons -2 (cons 5 (cons -7 (cons -8 empty))))))
              (cons 1 (cons 5 empty)))

(define extract
  (lambda (p? list)
    (cond
      ((empty? list) list)
      ((cons? list) (if (p? (first list))
                        (cons (first list) (extract p? (rest list)))
                        (extract p? (rest list)))))))

; Concatenate
; %element: Signaturvariable ... Generics
(: concat ((list-of %element) (list-of %element) -> (list-of %element)))

(check-expect (concat list1 list2)
              (cons 7(cons 3 (cons 7 empty))))
(check-expect (concat empty list1)
              list1)
(check-expect (concat list2 empty)
              list2)
              
(define concat
  (lambda (list1 list2)
    (cond
      ((empty? list1) list2)
      ; ((empty? list2) list1)
      ((cons? list1) (cons (first list1)
                           (concat (rest list1) list2))))))

; ehrenhafter Fehlversuch
#;(define concat2
  (lambda (list1 list2)
    (cond
      ((empty? list2) ...)
      ((cons? list2)
       (first list2) ; 3
       (concat2 list1 (rest list2)))))) ; 7 7, gewünscht: 7 3 7

; (: cons (%element (list-of %element) -> (list-of %element)))
; (: list (%element ... -> (list-of %element)))

(define inc
  (lambda (n)
    (+ 1 n)))

(define inc5
  (lambda (n)
    (+ 5 n)))

; Funktionsfabrik, "Spezialisierung"
(: incx (number -> (number -> number)))
(define incx
  (lambda (x)
    (lambda (n)
      (+ x n))))

(define mult2
  (lambda (n)
    (* 2 n)))
(define mult3
  (lambda (n)
    (* 3 n)))
(define multx
  (lambda (x)
    (lambda (n)
      (* x n))))

(: specialize ((%a %b -> %c) %a -> (%b -> %c)))

(define specialize
  (lambda (f x)
    (lambda (n)
      (f x n))))

(: schönfinkeln ((%a %b -> %c) -> (%a -> (%b -> %c))))

(define schönfinkeln
  (lambda (f)
    (lambda (x)
      (lambda (n)
        (f x n)))))
(define curry schönfinkeln)

(: entschönfinkeln ((%a -> (%b -> %c)) -> (%a %b -> %c)))

(define entschönfinkeln
  (lambda (f)
    (lambda (a b)
      ((f a) b))))

(define s+ (schönfinkeln +))

(check-property
 (for-all ((f (string number -> boolean)) ; leider keine Signaturvariablen möglich
           (a string)
           (b number))
   (boolean=?
    (f a b)
    (((schönfinkeln f) a) b))))

(map inc (list 1 2 3))
;(map (lambda (n) (+ 5 n)) (list 1 2 3))
(map (incx 5) (list 1 2 3))
(map (specialize + 5) (list 1 2 3))