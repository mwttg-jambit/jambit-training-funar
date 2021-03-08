package funar

sealed trait Suit
object Suit {
  case object Diamonds extends Suit
  case object Clubs extends Suit
  case object Spades extends Suit
  case object Hearts extends Suit

  val all = Seq(Diamonds, Clubs, Spades, Hearts)
}

// total geordnet
//sealed trait Rank extends Ordered[Rank] { // Alternative abstract class Rank(value: Int)
sealed abstract class Rank extends Ordered[Rank] {
  val value: Int

  // Ordered
  override def compare(other: Rank): Int =
    this.value - other.value
} 
object Rank {
  case object Two extends Rank { val value = 2 }
  case object Three extends Rank  { val value = 3 }
  case object Four extends Rank { val value = 4 }
  case object Five extends Rank { val value = 5 }
  case object Six extends Rank { val value = 6 }
  case object Seven extends Rank { val value = 7 }
  case object Eight extends Rank { val value = 8 }
  case object Nine extends Rank { val value = 9 }
  case object Ten extends Rank { val value = 10 }
  case object Jack extends Rank { val value = 11 }
  case object Queen extends Rank { val value = 12 }
  case object King extends Rank { val value = 13 }
  case object Ace extends Rank { val value = 14 }

  implicit val heartsOrdering: Ordering[Rank] = Ordering.by(- _.value)

  val all: Seq[Rank] = Seq(Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, 
                           Jack, Queen, King, Ace)


  val disordered = Seq(Three, Jack, Six, Nine, Two, Ace)

  val ordered = disordered.sorted(heartsOrdering)

}

// "case"
// - mit match verwendbar
// - Standard-Konstruktor
// - Standard-Implementierungen für equals, hashCode, toString
// - Factory-Methode Card.apply
case class Card(suit: Suit, rank : Rank) {
  // Ist diese Karte höherwertig als andere Karte gleicher Farbe
  def beats(other: Card): Option[Boolean] =  // wie Maybe in Haskell
    if (this.suit == other.suit)
      Some(this.rank > other.rank) // dank Ordering
    else
      None
}

object Card {
  def cartesianProduct[A, B](as: Seq[A], bs: Seq[B]): Seq[(A, B)] = // Seq von 2-Tupeln aus jeweils A und B
    as.map { a => bs.map { b => (a, b)}}.flatten // Seq[Seq[X]] => Seq[X]

  def genCartesianProduct[A, B, C](f: (A, B) => C, as: Seq[A], bs: Seq[B]): Seq[C] = // Seq von 2-Tupeln aus jeweils A und B
    as.map { a => bs.map { b => f(a, b)}}.flatten // Seq[Seq[X]] => Seq[X]

//  def deck: Seq[Card] =
//    cartesianProduct(Suit.all, Rank.all).map { // 1stellige Funktion, deren Rumpf aus Pattern-Matching besteht
//        case (suit, rank) => Card(suit, rank)
//    }
//    cartesianProduct(Suit.all, Rank.all).map { pair => Card(pair._1, pair._2)}

  // Funktion in Scala: Objekt mit .apply-Methode
  def deck: Seq[Card] = genCartesianProduct(Card.apply, Suit.all, Rank.all)

}