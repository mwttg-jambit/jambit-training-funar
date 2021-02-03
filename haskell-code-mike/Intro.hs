module Intro where

x :: Integer
x = 42

-- Ein Haustier ist eins der folgenden:
-- - Hund
-- - Katze
-- - Schlange
-- data: neuer Datentyp
data Pet = Dog | Cat | Snake
    deriving Show

-- Großbuchstaben: "konstant"
-- Kleinbuchstaben: Variable

-- Ist Haustier niedlich?
isCute :: Pet -> Bool
isCute Dog = True
isCute Cat = True
isCute Snake = False

-- Aggregatzustand ist eins der folgenen:
-- - fest
-- - flüssig
-- - gas
data State = Solid | Liquid | Gas
  deriving Show

waterState :: Double -> State
waterState temp =
    if temp < 0
    then Solid
    else if temp < 100
    then Liquid
    else Gas

-- Ein Gürteltier:
-- - tot oder lebendig
-- - Gewicht
data Liveness = Dead | Alive
  deriving Show

{-
data Dillo = Dillo { dilloLiveness :: Liveness, dilloWeight :: Double }
--   ^^^^Typ  ^^^^ Konstruktor
  deriving Show

-- Gürteltier, lebendig, 10kg
dillo1 :: Dillo
dillo1 = Dillo { dilloLiveness = Alive, dilloWeight = 10 }
-- Gürteltier, tot, 8kg
dillo2 :: Dillo
dillo2 = Dillo Dead 8

-- Gürteltier überfahren
runOverDillo :: Dillo -> Dillo
-- runOverDillo dillo =
--    Dillo { dilloLiveness = Dead, dilloWeight = dilloWeight dillo }
-- runOverDillo dillo = Dillo Dead (dilloWeight dillo)
-- runOverDillo (Dillo liveness weight) = Dillo Dead weight
-- runOverDillo (Dillo _ weight) = Dillo Dead weight
-- runOverDillo (Dillo {dilloLiveness = liveness, dilloWeight = weight }) = Dillo Dead weight 
runOverDillo dillo = dillo { dilloLiveness = Dead }

-- Ein Papagei hat folgende Eigenschaften:
-- - Satz
-- - Gewicht
data Parrot = Parrot String Double
  deriving Show

-- Papagei überfahren
runOverParrot :: Parrot -> Parrot
runOverParrot (Parrot sentence weight) = Parrot "" weight
-}

-- Ein Tier ist eins der folgenden:
-- - ein Gürteltier
-- - ein Papagei
-- algebraischer Datentyp
data Animal =
    Dillo { dilloLiveness :: Liveness, dilloWeight :: Double}
  | Parrot String Double
  deriving Show

dillo1 :: Animal
dillo1 = Dillo Alive 10
dillo2 :: Animal
dillo2 = Dillo Dead 8
parrot1 :: Animal
parrot1 = Parrot "Hallo!" 1
parrot2 :: Animal
parrot2 = Parrot "Goodbye!" 2

-- Tier überfahren
runOverAnimal :: Animal -> Animal
-- Gleichungen werden nacheinander ausprobiert
-- NICHT:
-- runOverAnimal dillo = dillo { dilloLiveness = Dead }
-- OK:
-- runOverAnimal dillo@(Dillo {}) =  dillo { dilloLiveness = Dead }
runOverAnimal (Dillo _ weight) = Dillo Dead weight
runOverAnimal (Parrot _ weight) = Parrot "" weight
-- runOverAnimal dillo = dillo { dilloLiveness = Dead }

-- Tier füttern
feedAnimal :: Double -> Animal -> Animal
feedAnimal amount (Dillo liveness weight) =
    case liveness of
      Dead -> Dillo liveness weight
      Alive -> Dillo liveness (weight + amount)
feedAnimal amount (Parrot sentence weight) =
    Parrot sentence (weight + amount)

feed5 :: Animal -> Animal
feed5 = feedAnimal 5

feedAnimal' :: (Double, Animal) -> Animal
feedAnimal' (amount, Dillo liveness weight) =
    case liveness of
      Dead -> Dillo liveness weight
      Alive -> Dillo liveness (weight + amount)
feedAnimal' (amount, Parrot sentence weight) =
    Parrot sentence (weight + amount)

{-
(: schönfinkeln ((%a %b -> %c) -> (%a -> (%b -> %c))))

(define schönfinkeln
  (lambda (f)
    (lambda (x)
      (lambda (n)
        (f x n)))))

-}
schoenfinkeln :: ((a, b) -> c) -> (a -> (b -> c))
-- schoenfinkeln f = \ x -> \ n -> f (x, n)
schoenfinkeln f x n = f (x, n)

entschoenfinkeln :: (a -> b -> c) -> ((a, b) -> c)
entschoenfinkeln f =
    \ (a, b) -> (f a) b

-- Ein Liste ist eins der folgenden:
-- - die leere Liste
-- - eine Cons-Liste aus erstem Element und Rest-Liste
{-
data List a =
    Empty
  | Cons a (List a)
  deriving Show
-}

-- die leere Liste: []
-- Cons-Liste: first : rest
-- [1,2,3,4]

-- Listelemente aufsummieren
listSum :: [Double] -> Double
listSum [] = 0
listSum (first:rest) = first + (listSum rest)

highway = [dillo1, parrot1, dillo2, parrot2]

listFold :: b -> (a -> b -> b) -> [a] -> b
listFold forEmpty forCons [] = forEmpty
listFold forEmpty forCons (x :                                    xs) = 
                           x `forCons` (listFold forEmpty forCons xs)

{-
Java, Racket:
Bei einem Funktions-/Methodenaufruf werden zunächst alle Argumente ausgewertet,
bevor die Ausführung mit dem Rumpf der Funktion weitergeht.
strikte Programmiersprachen

Haskell:
Ein Ausdruck wird erst dann ausgewertet, wenn der Wert benötigt wird.
-}
natsFrom :: Integer -> [Integer]
natsFrom n = n : (natsFrom (n+1))

-- alle Vielfachen einer Zahl aus einer Liste entfernen
strikeMultiples :: Integer -> [Integer] -> [Integer]
strikeMultiples n ns = filter (\ x -> x `mod` n /= 0) ns

-- Eingabe: Liste von Primzahl-Kandidaten, bei denen die erste Zahl
-- als Primzahl identifiziert ist
-- Ausgabe: Primzahlen
sieve (prime:rest) = prime : (sieve (strikeMultiples prime rest))

primes = sieve (natsFrom 2)

