package funar

object Monads {

  trait Functor[F[_]] {
    def map(x: F[A])(f: A => B): F[B]
  }

  // geht nur für Typen mit Typparameter
  trait Monad[M[_]] { // higher-kinded type
    def pure[A](result: A): M[A]
    def flatMap[A, B](m: M[A])(f: A => M[B]): M[B]
  }

}