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
  case object Dead extends Liveness
  case object Alive extends Liveness
  
  case class Dillo(liveness: Liveness, weight: Int)
}
