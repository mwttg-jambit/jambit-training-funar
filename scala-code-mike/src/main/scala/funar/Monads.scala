package funar

object Monads {
  // geht nur für Typen mit Typparameter
  trait Monad[M[_]] { // higher-kinded type
    def pure[A](result: A): M[A]
    def flatMap[A, B](m: M[A])(f: A => M[B]): M[B]
  }

}