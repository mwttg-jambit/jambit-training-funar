package funar

import scala.annotation.tailrec

object Intro {
  val s: String = "Mike"

  // data Pet = Dog | Cat | Snake
  // gemischte Daten / Aufzählungen:
  // sealed: keine Unterklassen außer denen in dieser Datei
  // trait ... interface
  sealed trait Pet
  // Aufzählung:
  case object Cat extends Pet
  case object Dog extends Pet
  case object Snake extends Pet

  // Es gibt "eingebaute" Aufzählungen in Scala
  // Die sind Mist.
  val p1: Pet = Cat
  val p2 = Dog

  // Ist ein Haustier niedlich?
  def isCute(pet: Pet): Boolean =
    pet match {
      case Cat => true
      case Dog => true
      case Snake => false
    }
    
  sealed trait Liveness
  object Liveness {
    case object Dead extends Liveness
    case object Alive extends Liveness
  }

  // "value class / value objects"
  sealed trait Animal {
    def runOver: Animal

    def runOver2: Animal =
      this match {
        case Dillo(liveness, weight) => Dillo(Liveness.Dead, weight)
        case Parrot(sentence, weight) => Parrot("", weight)
      }
  }
  case class Dillo(liveness: Liveness, weight: Int) extends Animal {
    def runOver() = Dillo(Liveness.Dead, this.weight)
  }
  case class Parrot(sentence: String, weight: Int) extends Animal {
    def runOver() = Parrot("", this.weight)
  }

  val d1 = Dillo(Liveness.Alive, 10) // Gürteltier, lebendig, 10 kg
  val d2 = Dillo(Liveness.Dead, 12)
  val parrot1 = Parrot("Hello", 1)
  val parrot2 = Parrot("Goodbye", 2)

  // Gürteltier überfahren
  def runOverDillo(dillo: Dillo): Dillo =
    // Dillo(Liveness.Dead, dillo.weight)
    // dillo.copy(liveness = Liveness.Dead)
    dillo match {
      case Dillo(liveness, weight) => Dillo(Liveness.Dead, weight)
    }

  // Tier überfahren
  def runOverAnimal(animal: Animal): Animal =
    animal match {
      case Dillo(liveness, weight) => Dillo(Liveness.Dead, weight)
      // case Parrot(sentence, weight) => Parrot("", weight)
      case parrot: Parrot => parrot.copy(sentence = "")
    }

  // Eine Liste ist eins der folgenden:
  // - die leere Liste - Nil
  // - die Cons-Liste aus erstem Element und Rest, Konstruktor ::

  val highway: List[Animal] = d1 :: (d2 :: (parrot1 :: (parrot2 :: Nil)))
  val animals = List(d1, parrot1)

  // Ein nicht-tail-call / Aufruf mit Kontext benötigt Speicherplatz zur Laufzeit
  // für Aktivierungsrecord / Frame
  // JVM: Stack fester Größe, klein im Verhältnis zum Gesamtspeicher
  // JVM: auch Tail-Calls verbrauchen Platz auf dem Stack

  // def runOverAnimals(animals: List[Animal]): List[Animal] =
  //   animals match {
  //     case Nil => Nil
  //     case first::rest =>
  //        runOverAnimal(first) :: runOverAnimals(rest)
  //   }

  def runOverAnimals(animals: List[Animal]): List[Animal] = {
    @tailrec
    def loop(animals: List[Animal], res: List[Animal]): List[Animal] =
      animals match {
        // reverse hat keine Klammern
        case Nil => res.reverse // Konvention in Scala für Funktionen/Methoden ohne Seiteneffekte
        case first::rest =>
          loop(rest, runOverAnimal(first) :: res)
      }
    loop(animals, Nil)
  }

  // Version mit Akkumulator
  // res: alle Tiere überfahren, die bisher schon gesehen wurden
  @tailrec
  def runOverAnimals1(animals: List[Animal], res: List[Animal]): List[Animal] =
    animals match {
      case Nil => res.reverse // Konvention in Scale für Funktionen/Methoden ohne Seiteneffekte
      case first::rest =>
        runOverAnimals1(rest, runOverAnimal(first) :: res)
    }

  def map[A](f: A => A, list: List[A]): List[A] = {
    @tailrec
    def loop(list: List[A], res: List[A]): List[A] =
      list match {
        case Nil => res.reverse // Konvention in Scala für Funktionen/Methoden ohne Seiteneffekte
        case first::rest =>
          loop(rest, f(first) :: res)
      }
    loop(list, Nil)
  }
  
  val ex1 = highway.map(runOverAnimal)
  
  val dillos: List[Dillo] = List(d1, d2)
  // Hinweis: == ruft equals-Methode auf
  val ex2 = dillos.filter({dillo => dillo.liveness == Liveness.Alive} )
  // geschweifte Klammern weglassen
  val ex2_1 = dillos.filter( dillo => dillo.liveness == Liveness.Alive )
  // runde Klammern weglassen - wenn Funktion oder Methode nur ein Argument
  val ex2_2 = dillos.filter { dillo => dillo.liveness == Liveness.Alive }
  // implizit Funktion mit einem Parameter namens _
  // (geht auch mit mehreren Parameter, heißen alle _)
  val ex2_3 = dillos.filter( _.liveness == Liveness.Alive )

  val list1: List[Int] = List(1,2,3)
  val list2: List[Animal] = List(d1, parrot1)

  val ex3 = list1.foldRight(0)((a, b) => a + b)

}
