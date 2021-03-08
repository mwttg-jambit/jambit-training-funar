;; Die ersten drei Zeilen dieser Datei wurden von DrRacket eingefügt. Sie enthalten Metadaten
;; über die Sprachebene dieser Datei in einer Form, die DrRacket verarbeiten kann.
#reader(lib "vanilla-reader.rkt" "deinprogramm" "sdp")((modname rev) (read-case-sensitive #f) (teachpacks ()) (deinprogramm-settings #(#f write repeating-decimal #f #t none explicit #f ())))
; Liste umdrehen
(: rev ((list-of %a) -> (list-of %a)))

#;(check-expect (rev (list 1 2 3))
              (list 3 2 1))

(check-property
 (for-all ((a (list-of string))
           (b (list-of string)))
   (expect (rev (concat a b))
           (concat (rev b) (rev a)))))

(define rev
  (lambda (list)
    (cond
     ((empty? list) empty)
     ((cons? list) 
      (concat (rev (rest list))
              (cons (first list) empty)))))) ; #<list 3 2>

; n + (n-1) + (n-2) + ... + 1 Aufrufe, wenn n Listenlänge
; n * (n + 1) / 2 = n^2 + ...

(: concat ((list-of %element) (list-of %element) -> (list-of %element)))

(check-property
 (for-all ((a (list-of string))
           (b (list-of string))
           (c (list-of string)))
   (expect (concat a (concat b c))
           (concat (concat a b) c))))

(check-property
 (for-all ((a (list-of string)))
   (expect (concat a empty)
           a)))

(define concat
  (lambda (list1 list2)
    (cond
      ((empty? list1) list2)
      ; ((empty? list2) list1)
      ((cons? list1) (cons (first list1)
                           (concat (rest list1) list2))))))

(define rev-2
  (lambda (list0)
    (rev* list0 empty)))

(define rev*
  (lambda (list res) ; Zwischenergebnis: bisher gesehene Listenelemente, umgedreht
    ; Schleifeninvariante: Aussage, die list0, list und res in Beziehung setzen
    ; res enthält die Elemente von list0 bis list in umgekehrter Reihenfolge
    (cond
      ((empty? list) res)
      ((cons? list)
       ; tail call, Aufruf ohne Kontext, endrekursiver Aufruf
       (rev* (rest list) (cons (first list) res))))))

; (rev-2 (list 1 2 3 4 5))

(: extract-evens ((list-of number) -> (list-of number)))

(check-expect (extract-evens (cons 1 (cons 4 (cons 7 (cons 10 (cons 12 empty))))))
              (cons 4 (cons 10 (cons 12 empty))))

#;(define extract-evens
  (lambda (list)
    (cond
      ((empty? list) empty)
      ((cons? list) (if (even? (first list))
                        (cons (first list) (extract-evens (rest list)))
                        (extract-evens (rest list)))))))

(define extract-evens
  (lambda (list0)
    (rev (extract-evens* list0 empty))))

(define extract-evens*
  (lambda (list res)
    ; res enthält die geraden Zahlen unter den Listenelementen zwischen list0 und list
    (cond
      ((empty? list) res)
      ((cons? list)
       (extract-evens* (rest list)
                       (if (even? (first list))
                           (cons (first list) res)
                           res))))))