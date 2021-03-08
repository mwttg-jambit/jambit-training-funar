package funar

/*
Methode:
1. einfache Beispiele für Domänenobjekte einholen
2. die modellieren -> möglicherweise Sackgasse
3. einfachen Beispiele in "atomare" Bestandteile zerlegen
*/

/*
Einfacher Vertrag:

Bekomme 100 Pfund am 29.1.2001

Bekomme 200 EUR am 31.12.2021

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

object Contract {
   type Amount = Double

   // val zcb1 = ZeroCouponBond(100, Currency.GBP, Date("2001-01-29"))
}