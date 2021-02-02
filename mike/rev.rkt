;; Die ersten drei Zeilen dieser Datei wurden von DrRacket eingefügt. Sie enthalten Metadaten
;; über die Sprachebene dieser Datei in einer Form, die DrRacket verarbeiten kann.
#reader(lib "vanilla-reader.rkt" "deinprogramm" "sdp")((modname rev) (read-case-sensitive #f) (teachpacks ()) (deinprogramm-settings #(#f write repeating-decimal #f #t none explicit #f ())))
; Liste umdrehen
(: rev ((list-of %a) -> (list-of %a)))

(check-expect (rev (list 1 2 3))
              (list 3 2 1))

(define rev
  (lambda (list)
    (cond
     ((empty? list) empty)
     ((cons? list) 
      (concat (rev (rest list))
              (cons (first list) empty)))))) ; #<list 3 2>

; n + (n-1) + (n-2) + ... + 1 Aufrufe, wenn n Listenlänge
; n * (n + 1) / 2 = n^2 + ...

(define concat
  (lambda (list1 list2)
    (cond
      ((empty? list1) list2)
      ; ((empty? list2) list1)
      ((cons? list1) (cons (first list1)
                           (concat (rest list1) list2))))))

(define rev*
  (lambda (list res) ; Zwischenergebnis: bisher gesehene Listenelemente, umgedreht
    (cond
      ((empty? list) res)
      ((cons? list)
       (rev* (rest list) (cons (first list) res))))))