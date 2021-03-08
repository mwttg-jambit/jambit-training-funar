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

type Amount = Double

case class ZeroCouponBond(amount: Amount)