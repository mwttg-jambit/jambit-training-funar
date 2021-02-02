;; Die ersten drei Zeilen dieser Datei wurden von DrRacket eingefügt. Sie enthalten Metadaten
;; über die Sprachebene dieser Datei in einer Form, die DrRacket verarbeiten kann.
#reader(lib "vanilla-reader.rkt" "deinprogramm" "sdp")((modname intro) (read-case-sensitive #f) (teachpacks ((lib "image.rkt" "teachpack" "deinprogramm" "sdp"))) (deinprogramm-settings #(#f write repeating-decimal #f #t none explicit #f ((lib "image.rkt" "teachpack" "deinprogramm" "sdp")))))
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

; (: image-width (image -> natural))
; (: image-height (image -> natural))

; Ein Bildformat ist eins der folgenden:
; - hochkant
; - quer
; - quadratisch
(define image-format
  (signature (enum "hochkant" "quer" "quadratisch")))

; Format eines Bildes ausrechnen
(: compute-image-format (image -> image-format))

(check-expect (compute-image-format (rectangle 100 50 "solid" "blue"))
              "quer")
(check-expect (compute-image-format (rectangle 10 50 "solid" "blue"))
              "hochkant")
(check-expect (compute-image-format (rectangle 10 10 "solid" "blue"))
              "quadratisch")

(define compute-image-format
  (lambda (image)
    (define width (image-width image))
    (cond
      ((> width (image-height image)) "quer")
      ((< width (image-height image)) "hochkant")
      #;((= width (image-height image)) "quadratisch")
      (else "quadratisch")

      )))

; Datendefinition
; Eine Uhrzeit besteht aus: / hat folgende Eigenschaften:
; - Stunde UND
; - Minute
; zusammengesetzte Daten
(define-record time ; Record-Signatur
  make-time ; Konstruktor
  (time-hour natural) ; Selektor
  (time-minute natural)) ; Selektor

(: make-time (natural natural -> time))
(: time-hour (time -> natural))
(: time-minute (time -> natural))

(define time1 (make-time 12 24)) ; 12 Uhr 24
(define time2 (make-time 16 12)) ; 16 Uhr 12

; Minuten seit Mitternacht berechnen
(: msm (time -> natural))

(check-expect (msm time1)
              (+ (* 12 60) 24))

; Schablone für zusammengesetzte Daten
#;(define msm
  (lambda (time)
    ... (time-hour time) ...
    ... (time-minute time) ...))

(define msm
  (lambda (time)
    (+ (* 60 (time-hour time))
       (time-minute time))))

; Tiere auf dem texanischen Highway

; Ein Tier ist eins der folgenden:
; - Gürteltier
; - Schlange
; Fallunterscheidung
; jeder Fall eigene zusammengesetzte Datendefinition:
; gemischte Daten
(define animal
  (signature
   (mixed dillo snake)))

; Ein Gürteltier hat folgende Eigenschaften:
; - tot oder lebendig
; - Gewicht
; Repräsentation des Zustands des Gürteltiers zu einem Zeitpunkt
(define-record dillo
  make-dillo ; Name reine Konvention
  dillo?
  (dillo-liveness liveness)
  (dillo-weight number))

(: make-dillo (liveness number -> dillo))
(: dillo-liveness (dillo -> liveness))
(: dillo-weight (dillo -> number))
(: dillo? (any -> boolean))

; Die "Lebendigkeit" ist eins der folgenden:
; - tot
; - lebendig
(define liveness
  (signature (enum "dead" "alive")))

(define dillo1 (make-dillo "alive" 10)) ; lebendiges Gürteltier, wiegt 10kg
(define dillo2 (make-dillo "dead" 8)) ; totes Gürteltier, 8kg

; Gürteltier überfahren
(: run-over-dillo (dillo -> dillo))

(check-expect (run-over-dillo dillo1)
              (make-dillo "dead" 10))
(check-expect (run-over-dillo dillo2)
              (make-dillo "dead" 8))

(define run-over-dillo
  (lambda (dillo)
    (make-dillo "dead" (dillo-weight dillo))))

; Gürteltier füttern
(: feed-dillo (dillo number -> dillo))

(check-expect (feed-dillo dillo1 3)
              (make-dillo "alive" 13))
(check-expect (feed-dillo dillo2 5)
              dillo2)

(define feed-dillo
  (lambda (dillo amount)
    (match (dillo-liveness dillo)
      ("alive" (make-dillo "alive" (+ (dillo-weight dillo) amount)))
      ("dead" dillo))))
                                                                     
        
                 


; Eine Schlange hat folgende Eigenschaften:
; - Länge
; - Dicke
(define-record snake
  make-snake
  snake?
  (snake-length number)
  (snake-thickness number))

(define snake1 (make-snake 200 10)) ; Schlange, 2m lang, 10cm dick
(define snake2 (make-snake 800 30)) ; Anaconda

; Schlange überfahren
(: run-over-snake (snake -> snake))

(check-expect (run-over-snake snake1)
              (make-snake 200 0))
(check-expect (run-over-snake snake2)
              (make-snake 800 0))

(define run-over-snake
  (lambda (snake)
    (make-snake (snake-length snake) 0)))

; Schlange überfahren
(: feed-snake (snake number -> snake))

(check-expect (feed-snake snake1 5)
              (make-snake 200 10.5))

(define feed-snake
  (lambda (snake amount)
    (make-snake (snake-length snake)
                (+ (snake-thickness snake) (* 0.1 amount)))))


; Tier überfahren
(: run-over-animal (animal -> animal))

(check-expect (run-over-animal dillo1)
              (run-over-dillo dillo1))
(check-expect (run-over-animal snake1)
              (run-over-snake snake1))

(define run-over-animal
  (lambda (animal)
    (cond
      ((dillo? animal) (run-over-dillo animal))
      ((snake? animal) (run-over-snake animal)))))

; Tier füttern
(: feed-animal (animal number -> animal))

(check-expect (feed-animal dillo1 5)
              (feed-dillo dillo1 5))
(check-expect (feed-animal snake1 5)
              (feed-snake snake1 5))

(define feed-animal
  (lambda (animal amount)
    (cond
      ((dillo? animal) (feed-dillo animal amount))
      ((snake? animal) (feed-snake animal amount)))))

(define highway (list dillo1 snake1 dillo2 snake2))

(map (lambda (animal) (feed-animal animal 2)) highway)


#|
class Dillo {
  Liveness liveness;
  double weight;

  void runOver() {
    this.liveness = Liveness.DEAD;
  }
}

|#


#|
class C {
  int m(int x) {
    x = 16;
    ... x ...
  }

  c.m(15)
}

|#