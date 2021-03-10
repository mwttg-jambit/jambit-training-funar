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

    def join[A](mm: M[M[A]]): M[A] = flatMap(mm)(x => x)
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

    // In Haskell >>=, ausgesprochen "bind"
    def flatMap[A, B](list: List[A])(f: A => List[B]): List[B] =
      list.map(f).flatten
  }

  // Either[A, B]
  // Left(a: A) extends Either[A, B]
  // Right(b: B) extends Either[A, B]
  // häufig: Either[Error, Result]
  
  def eitherFunctor[Error] = new Functor[Either[Error, *]] {
    def map[A, B](e: Either[Error, A])(f: A => B): Either[Error, B] =
      e match {
        case Left(error) => Left(error)
        case Right(a) => Right(f(a))
      }

  }

  def eitherMonad[Error] = new Monad[Either[Error, *]] {
    def pure[A](a: A): Either[Error, A] = Right(a)

    def flatMap[A, B](e: Either[Error, A])(f: A => Either[Error, B]): Either[Error, B] =
      e match {
        case Left(error) => Left(error)
        case Right(a) => f(a)
      }
  }

  def safeDivide(a: Int, b: Int): Either[String, Int] =
    if (b == 0)
      Left("divided by zero")
    else
      Right(a / b)

  for {
    d1 <- safeDivide(10, 3)
    d2 <- safeDivide(10, 0)
    d3 <- safeDivide(5, 2)
  } yield (d1 + d2 + d2)

  case class Reader[Env, A](process: Env => A) {
    def map[B](f: A => B): Reader[Env, B] = readerFunctor[Env].map(this)(f)
    def flatMap[B](f: A => Reader[Env, B]): Reader[Env, B] =
      readerMonad[Env].flatMap(this)(f)
  }

  def get[Env]: Reader[Env, Env] = Reader(x => x)

  def readerFunctor[Env] = new Functor[Reader[Env, *]] {
    def map[A, B](reader: Reader[Env, A])(f: A => B): Reader[Env, B] =
      Reader(env => f(reader.process(env)))
  } 

  def readerMonad[Env] = new Monad[Reader[Env, *]] {
    def pure[A](a: A): Reader[Env, A] = Reader(_ => a)
    def flatMap[A, B](reader: Reader[Env, A])(f: A => Reader[Env, B]): Reader[Env, B] =
      join(Reader(env => f(reader.process(env))))
  }

  for {
    val x = 1 + 2
    dbHandle <- get[Int]

  } yield x

}