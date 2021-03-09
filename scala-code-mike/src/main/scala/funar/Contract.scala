package funar

/*
Methode:
1. einfache Beispiele für Domänenobjekte einholen
2. die modellieren -> möglicherweise Sackgasse
3. einfache Beispiele in "atomare" Bestandteile zerlegen
4. nach Selbstreferenzen suchen
5. ggf. mit weiteren Beispiel wiederholen
6. nach binärem Operator suchen
7. wenn 6 erfolgreich: neutrales Element
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

case class Date(desc: String) extends Ordered[Date] {
  def compare(that: Date): Int =
    this.desc.compare(that.desc)
}

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
// binärer Operator, +, *, beside, overlay
case class Both(contract1: Contract, contract2: Contract) extends Contract
case object Zero extends Contract


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

   val zcb4 = Later(Date("2002-02-01"), Multiple(100, One(Currency.GBP)))

      // Pay(Pay(c)) ~~~ c
   val contract3 = Pay(Later(Date("2002-02-01"), Multiple(100, One(Currency.GBP))))

   // D1 aus dem Paper
   val d1 = Both(zcb1, contract3)

   val contract4 = Multiple(100, Zero)

   val contract5 = Pay(Both(zcb1, zcb4))

   // smart constructor
   def multiple(amount: Amount, contract: Contract): Contract =
     contract match {
       case Zero => Zero
       case _ => Multiple(amount, contract) 
     }

   def both(contract1: Contract, contract2: Contract): Contract =
     (contract1, contract2) match {
       case (Zero, _) => contract2
       case (_, Zero) => contract1
       case _ => Both(contract1, contract2)
     }

   sealed trait Direction {
     def invert: Direction
   }
   case object Long extends Direction {
     def invert = Short
   }
   case object Short extends Direction {
     def invert = Long
   }

   case class Payment(direction: Direction, date: Date, amount: Amount, currency: Currency) {
     def invert =
      this.copy(direction = direction.invert)

     def scale(factor: Double): Payment =
       this.copy(amount = amount * factor)
   }
   
   // operationelle Semantik: zeitliche Entwicklung der Domänenobjekte
   // (vs. denotationalle Semantik: Domänenobjekt auf mathematisches Objekt abbilden)

   case class ContractInProgress(contract: Contract, payment: Seq[Payment])

   // Zahlungen bis now
   def semantics(contract: Contract, now: Date): (Seq[Payment], Contract) =
      contract match {
        case Zero => (Seq.empty, Zero)
        case One(currency) =>
          (Seq(Payment(Long, now, 1, currency)), Zero)
        case Multiple(amount, contract) =>
          val (payments, residualContract) = semantics(contract, now)
          (payments.map(_.scale(amount)), multiple(amount, residualContract))
        case Later(date, contract) =>
          if (now >= date)
            semantics(contract, now)
          else
            (Seq.empty, Later(date, contract))
        case Pay(contract) =>
          val (payments, residualContract) = semantics(contract, now)
          (payments.map(_.invert), Pay(residualContract))
        case Both(contract1, contract2) =>
          val (payments1, residualContract1) = semantics(contract1, now)
          val (payments2, residualContract2) = semantics(contract2, now)
          (payments1 ++ payments2, both(residualContract1, residualContract2))
      }

/*
Gruppe:
Mathematik:
- Menge M
- binäre Operation op, assoziativ: op(a, op(b, c)) ~~ op(op(a, b), c)
- ^^^ Halbgruppe / semigroup
- neutrales Element n bezüglich op: op(n, x) ~~ x ~~ op(x, n)
- ^^^ Monoid
- zu jedem Element n ein inverses Element n^-1: op(x, x^-1) = n
- ^^^ Gruppe

Programmierung:
statt Menge M gibt es einen Typ M
*/
/*
  // 1. Versuch
  trait Semigroup[M] {
    def op(other: M): M
  }
  
  trait Monoid[M] extends Semigroup[M] {
    def n: M
  }
*/
  trait Semigroup[M] {
    def op(a: M, b: M): M
  }
}