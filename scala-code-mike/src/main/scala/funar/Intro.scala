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

  val p1: Pet = Cat
  val p2 = Dog

  // Ist ein Haustier niedlich?
  def isCute(pet: Pet): Boolean =
    pet match {
      case Cat => true
      case Dog => true
      case Snake => false
    }
}
