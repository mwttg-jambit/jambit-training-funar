package funar

/*
Methode:
1. einfache Beispiele für Domänenobjekte einholen
2. die modellieren
*/

/*
Einfacher Vertrag:

Bekomme 100Pfund am 29.1.2001

Bekomme 200EUR am 31.12.2021

Zero-Coupon Bond
Zero-Bond
*/

import Contract.Amount

sealed trait Currency
object Currency {
  case object EUR extends Currency
  case object GBP extends Currency
}

case class Date(desc: String)

case class ZeroCouponBond(amount: Amount, currency: Currency, date: Date)

object Contract {
   type Amount = Double

   val zcb1 = ZeroCouponBond(100, Currency.GBP, Date("2001-01-29"))
}