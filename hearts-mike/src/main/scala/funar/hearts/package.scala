package funar.hearts

object `package` {
  type Hand = Set[Card]

  // oberste Karte zuerst
  type Trick = List[(Player, Card)]

  type PlayerHands = Map[Player, Hand]
}

