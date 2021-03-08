package funar

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
    def runOver(): Animal

    def runOver2(): Animal =
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
  def runOverAnimals(animals: List[Animal]): List[Animal] =
    animals match {
      case Nil => Nil
      case first::rest =>
         runOverAnimal(first) :: runOverAnimals(rest)
    }

  // Version mit Akkumulator
  // res: alle Tiere überfahren, die bisher schon gesehen wurden
  def runOverAnimals1(animals: List[Animal], res: List[Animal]): List[Animal] =
    animals match {
      case Nil => res
      case first::rest =>
        runOverAnimals(rest, runOverAnimal(first) :: res)
    }
}
