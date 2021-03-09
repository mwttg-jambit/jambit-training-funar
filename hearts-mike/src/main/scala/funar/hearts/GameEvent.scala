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

Gibt häufig auch noch Commands:
- Wunsch, daß etwas in der Zukunft passieren soll

BITTE NICHT DIESELBEN TYPEN / OBJEKTE!

*/
sealed trait GameEvent
object GameEvent {
}

sealed trait GameCommand
object GameCommand {
}
