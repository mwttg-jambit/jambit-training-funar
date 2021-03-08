package funar

sealed trait Suit
object Suit {
  case object Diamonds extends Suit
  case object Clubs extends Suit
  case object Spades extends Suit
  case object Hearts extends Suit

  val all = Seq(Diamonds, Clubs, Spades, Hearts)
}

sealed trait Rank 
object Rank {
  case object Two extends Rank
  case object Three extends Rank
  case object Four extends Rank
  case object Five extends Rank
  case object Six extends Rank
  case object Seven extends Rank
  case object Eight extends Rank
  case object Nine extends Rank
  case object Ten extends Rank
  case object Jack extends Rank
  case object Queen extends Rank
  case object King extends Rank
  case object Ace extends Rank

  val all: Seq[Rank] = Seq(Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, 
                           Jack, Queen, King, Ace)
}

case class Card(suit: Suit, rank : Rank)

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

  def deck: Seq[Card] = genCartesianProduct((_), Suit.all, Rank.all)

}