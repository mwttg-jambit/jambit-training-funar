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
  case class HandDealt(player: Player, hand: Hand) extends GameEvent
  case class PlayerTurnChanged(player: Player) extends GameEvent
  case class LegalCardPlayed(player: Player, card: Card) extends GameEvent
  case class IllegalCardPlayed(player: Player, card: Card) extends GameEvent
  case class TrickTaken(player: Player, trick: Trick) extends GameEvent
  case class GameEnded(won: Player) extends GameEvent
}

sealed trait GameCommand
object GameCommand {
  case class DealHands(hands: PlayerHands) extends GameCommand
  case class PlayCard(player: Player, card: Card) extends GameCommand
}
