package funar.hearts

sealed trait GameEvent
object GameEvent {
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