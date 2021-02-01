;; Die ersten drei Zeilen dieser Datei wurden von DrRacket eingefügt. Sie enthalten Metadaten
;; über die Sprachebene dieser Datei in einer Form, die DrRacket verarbeiten kann.
#reader(lib "beginner-reader.rkt" "deinprogramm" "sdp")((modname intro) (read-case-sensitive #f) (teachpacks ((lib "image.rkt" "teachpack" "deinprogramm" "sdp"))) (deinprogramm-settings #(#f write repeating-decimal #f #t none explicit #f ((lib "image.rkt" "teachpack" "deinprogramm" "sdp")))))
; Hier: Editor, unten: REPL
(define x (+ 12 42))
(define y
  (+ 12
     (* 13 56)
     (+ 17
        (* 56 12))))

(: star1 image)
(define star1 (star 50 "solid" "blue"))
(: square1 image)
(define square1 (square 100 "solid" "gold"))
(define circle1 (circle 50 "solid" "red"))

;(above
; (beside square1 circle1)
; (beside circle1 square1))

#;(above
 (beside star1 square1)
 (beside square1 star1))

; Konstruktionsanleitungen / design recipes
; Kurzbeschreibung
; quadratisches Kachelmuster aus zwei Bildern zusammensetzen
; Signaturdeklaration
(: tile (image image -> image))

; Testfall
(check-expect (tile square1 star1)
              (above
               (beside square1 star1)
               (beside star1 square1)))

(define tile
  (lambda (image1 image2) ; Parameter für eine Funktion
    (above
     (beside image1 image2)
     (beside image2 image1))))

;(tile star1 circle1)

; Datendefinition
; Ein Haustier ist eins der folgenden: 
; - Hund ODER
; - Katze ODER
; - Schlange
; allgemein: Fallunterscheidung
; hier: Aufzählung
(define pet
  (signature (enum "dog" "cat" "snake")))

; Ist ein Haustier niedlich?
(: cute? (pet -> boolean))

(check-expect (cute? "dog") #t)
(check-expect (cute? "cat") #t)
(check-expect (cute? "snake") #f)

; Gerüst
#;(define cute?
  (lambda (pet)
    ...))

; Schablone
#;(define cute?
  (lambda (pet)
    (cond ; Verzweigung (wg. Fallunterscheidung)
      ; Jeder Zweig (Bedingung Ergebnis)
      ((string=? pet "dog") ...)
      ((string=? pet "cat") ...)
      ((string=? pet "snake") ...)
      )))

(define cute?
  (lambda (pet)
    (cond ; Verzweigung (wg. Fallunterscheidung)
      ; Jeder Zweig (Bedingung Ergebnis)
      ((string=? pet "dog") #t)
      ((string=? pet "cat") #t)
      ((string=? pet "snake") #f)
      )))


#|
class C {
  int m(int x) {
    x = 16;
    ... x ...
  }

  c.m(15)
}

|#