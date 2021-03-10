package funar

object Monads {
  // geht nur für Typen mit Typparameter
  trait Monad[M[_]] {
    def pure[A](result: A): M[A]
  }

}