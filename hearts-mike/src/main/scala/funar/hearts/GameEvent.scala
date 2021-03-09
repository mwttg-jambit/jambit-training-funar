package funar.hearts

/* Event-Sourcing

vs. Data Warehouse: aktueller Zustand, wird verändert,
Konsistenz durch Transaktionen
in verteiltem Setting schwer performant & korrekt zu bekommen

Event-Sourcing:
Logbuch über alles, was passiert ist in der Organisation: Events

- alles, was passiert IST: Vergangenheit
- fachlich motiviert
- darf redundant sein
- Events können sehr lange leben

Gibt häufig auch noch Commands:
- Wunsch, daß etwas in der Zukunft passieren soll

BITTE NICHT DIESELBEN TYPEN / OBJEKTE!

*/
sealed trait GameEvent
object GameEvent {
/*
  case class PlayerReceivedCards(player: Player, hand: Hand)
  case class PlayerPlayedCard(player: Player, card: Card)

  case class PlayerTurnChanged(player: Player)
  case class GameStarted(players: List[Player])
  case class PlayerReceivedTrick(player: Player, trick: Trick)
  case class TrickCompleted(trick: Trick)
  case class PlayerSent3Cards(player: Player, card1: Card, card2: Card, card3: Card)
  case class PlayerReceived3Cards(player: Player, card1: Card, card2: Card, card3: Card)
  case class AllCardsPlayed()
  case class GameFinished(winner: Player)
  case class TrickSuitDetermined(suit: Suit)
  case class StartPlayerDetermined(player: Player)
*/
}

sealed trait GameCommand
object GameCommand {
}
