package funar

/*
Methode:
1. einfache Beispiele für Domänenobjekte einholen
2. die modellieren -> möglicherweise Sackgasse
3. einfache Beispiele in "atomare" Bestandteile zerlegen
4. nach Selbstreferenzen suchen
5. ggf. mit weiteren Beispiel wiederholen
*/

/*
Einfacher Vertrag:

Bekomme 100 Pfund am 29.1.2001

Bekomme 200 EUR am 31.12.2021

Bezahle 100 Pfund am 1.2.2002

Zero-Coupon Bond
Zero-Bond

3 Ideen:
- "später"
- "Währung"
- "Betrag"

*/

import Contract.Amount

sealed trait Currency
object Currency {
  case object EUR extends Currency
  case object GBP extends Currency
}

case class Date(desc: String)

sealed trait Contract
/*
case class ZeroCouponBond(amount: Amount, currency: Currency, date: Date) extends Contract
case class Everest()
case class Call()
case class Put()
case class Annapurna()
*/
case class One(currency: Currency) extends Contract // "Bekomme jetzt 1EUR"
//case class Multiple(amount: Amount, currency: Currency) extends Contract
case class Multiple(amount: Amount, contract: Contract) extends Contract
case class Later(date: Date, contract: Contract) extends Contract
// "dreht den Vertrag um"
case class Pay(contract: Contract) extends Contract
case class Both(contract1: Contract, contract2: Contract) extends Contract

object Contract {
   type Amount = Double

   // bekomme 100 EUR jetzt
   val contract1 = Multiple(100, One(Currency.EUR))

   // Es gelten Gleichungen, z.B. zcb1 ~~~ zcb2
   val zcb1 = Later(Date("2001-01-29"), Multiple(100, One(Currency.GBP)))
   val zcb2 = Multiple(100, Later(Date("2001-01-29"), One(Currency.GBP)))

   def zeroCouponBond(amount: Amount, currency: Currency, date: Date): Contract =
    Later(date, Multiple(amount, One(currency)))

   val zcb3 = zeroCouponBond(100, Currency.GBP, Date("2001-01-29"))

   // Pay(Pay(c)) ~~~ c
   val contract3 = Pay(Later(Date("2002-02-01"), Multiple(100, One(Currency.GBP))))
}