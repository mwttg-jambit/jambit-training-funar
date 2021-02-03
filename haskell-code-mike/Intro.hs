module Intro where

import Prelude hiding (Semigroup, Monoid, Functor)

-- x :: Integer
-- x = 42

-- Ein Haustier ist eins der folgenden:
-- - Hund
-- - Katze
-- - Schlange
-- data: neuer Datentyp
data Pet = Dog | Cat | Snake
    deriving Show

instance Eq Pet where
  (==) Dog Dog = True
  (==) Cat Cat = True
  (==) Snake Snake = True
  (==) _ _ = False 

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
  deriving (Show, Eq)

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

instance Eq Liveness where
  Dead == Dead = True
  Alive == Alive = True
  _ == _ = False


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

instance Eq Animal where
    (Dillo liveness1 weight1) == (Dillo liveness2 weight2) = 
        (liveness1 == liveness2) && (weight1 == weight2)
    (Parrot sentence1 weight1) == (Parrot sentence2 weight2) =
        (sentence1 == sentence2) && (weight1 == weight2)

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

data Map key value = Map { unMap :: [(key, value)] }
  deriving Show

mapMap :: (a -> b) -> (Map key) a -> (Map key) b
mapMap f (Map []) = Map []
mapMap f (Map ((key,value):rest)) =
--    case mapMap f (Map rest) of
--      Map rest' -> Map ((key, f value):rest')
  let Map rest' = mapMap f (Map rest)
  in Map ((key, f value):rest')

y = let a = 5
        b = 7
    in a + b

map1 :: Map String String
map1 = Map [("Mike", "Sperber"), ("Anton", "Schreck")]

{-
data Maybe a =
    Just a | Nothing
-}

-- Eq key: Constraint "Der Typ key muß Gleichheit unterstützen"
mapGet :: Eq key => key -> Map key value -> Maybe value
mapGet key (Map []) = Nothing
mapGet key (Map ((key', value'):rest)) =
    if key == key'
    then Just value'
    else mapGet key (Map rest)

-- Eq: Typklasse ---> eher wie "Interface"
-- Implementierung: Instanz / instance
-- eingebaut:
{-
class Eq a where
  - Äquivalenzrelation
  -- Symmetrie:
  -- a == b => b == a
  -- Transitivität
  -- (a == b) && (b == c) => a == c
  -- Reflexivität
  -- a == a
  (==) :: a -> a -> Bool
-}

class Semigroup a where
    -- a `combine` (b `combine` c) == (a `combine` b) `combine c
    combine :: a -> a -> a

instance Semigroup [a] where
    combine = (++)

class Semigroup a => Monoid a where
    -- a `combine` neutral == neutral `combine` a == a
    neutral :: a

instance Monoid [a] where
    neutral = []

-- listMap :: (a -> b) -> [a] -> [b]
-- listMap :: (a -> b) -> List a -> List b
listMap f [] = []
listMap f (x:xs) = (f x) : (listMap f xs)

maybeMap :: (a -> b) -> Maybe a -> Maybe b 
maybeMap f Nothing = Nothing
maybeMap f (Just result) = Just (f result)

identity x = x

-- f ist Maybe, List, Map key
class Functor f where
    -- universalMap identity x == x
    -- universalMap (f . g) == (universalMap f) . (universalMap g)
    universalMap :: (a -> b) -> f a -> f b

instance Functor Maybe where
    universalMap = maybeMap

instance Functor [] where
    universalMap = listMap

instance Functor (Map key) where
    universalMap = mapMap

{-
   put "Mike" 17
   x = get "Mike"
   put "Mike" (x + 1)
   y = get "Mike"

   result (x+y)
-}

{-
data DBCommand =
    Put String Integer
  | Get String

type DBProgram = [DBCommand]

p1 = [Put "Mike" 17,
      Get "Mike",
      Put "Mike" (x+1)]
-}

data DB =
    Get String         (Integer -> DB)
  | Put String Integer (()      -> DB)
  | Result Integer


p1 = Put "Mike" 17 (\() ->
     Get "Mike" (\x ->
     Put "Mike" (x + 1) (\() ->
     Get "Mike" (\y ->
     Result (x+y)))))