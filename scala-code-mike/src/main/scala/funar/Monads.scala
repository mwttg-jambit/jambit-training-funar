package funar

object Monads {

  trait Functor[F[_]] {
    def map[A, B](x: F[A])(f: A => B): F[B]
  }

  // Jede Monade ist ein Funktor, aber es gibt auch
  // Funktoren, die keine Monade sind.

  // dazwischen: Applicative Functors
  //             Selective Functors

  // geht nur für Typen mit 1 Typparameter
  trait Monad[M[_]] { // higher-kinded type
    def pure[A](result: A): M[A]
    def flatMap[A, B](m: M[A])(f: A => M[B]): M[B]
  }

  // einstellige Typkonstruktoren
  // Option[_]
  // List[_]

  def listFunctor = new Functor[List] {
    def map[A, B](x: List[A])(f: A => B): List[B] =
      x.map(f)
  } 

  def listMonad = new Monad[List] {
    def pure[A](a: A): List[A] = List(a)

    def flatMap[A, B](list: List[A])(f: A => List[B]): List[B] =
      list.map(f).flatten
  }

}