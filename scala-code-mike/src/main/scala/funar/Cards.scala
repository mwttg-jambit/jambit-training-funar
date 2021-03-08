package funar

sealed trait Suit
object Suit {
  case object Diamonds extends Suit
  case object Clubs extends Suit
  case object Spades extends Suit
  case object Hearts extends Suit

  val all = Seq(Diamonds, Clubs, Spades, Hearts)
}

sealed trait Rank extends Ordered[Rank] { 
  val value : Int
  def compare(other: Rank) =
    this.value - other.value
}
object Rank {
  case object Two extends Rank { val value = 2 }
  case object Three extends Rank { val value = 3 }
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

  implicit val ordering: Ordering[Rank] = Ordering.by(_.value)

  val all: Seq[Rank] = Seq(Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, 
                           Jack, Queen, King, Ace)
}

case class Card(suit: Suit, rank : Rank)

object Card {
  
}