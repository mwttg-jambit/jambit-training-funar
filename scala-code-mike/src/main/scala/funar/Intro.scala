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
  case class Dillo(liveness: Liveness, weight: Int)

  val d1 = Dillo(Liveness.Alive, 10) // Gürteltier, lebendig, 10 kg
  val d2 = Dillo(Liveness.Dead, 12)

  // Gürteltier überfahren
  def runOverDillo(dillo: Dillo): Dillo =
    // Dillo(Liveness.Dead, dillo.weight)
    // dillo.copy(liveness = Liveness.Dead)
    dillo match {
      case Dillo(liveness, weight) => Dillo(Liveness.Dead, weight)
    }

}
