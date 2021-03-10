package funar

import cats._
import cats.implicits._ 
import cats.data._ 

object Free {
  // Reader, aber diesmal als Datentyp, unabhängig von der Implementierung
  // wie DB
  sealed trait Reader1[Env, A]
  case class Get1[Env, A](callback: Env => Reader1[Env, A]) extends Reader1[Env, A] 
  case class Return1[Env, A](result: A) extends Reader1[Env, A]

  def get1[Env] : Reader1[Env, Env] = Get1(Return1(_))

  // F ist nachher zuständig für die konkreten Operationen
  // F ist entweder DB', Reader'[Env, *]
  sealed trait Free[F[_], A]
  case class Pure[F[_], A](result: A) extends Free[F, A] // Return1 / Return
  case class Impure[F[_], A](f: F[Free[F, A]]) extends Free[F, A]

  sealed trait ReaderF[Env, SelfReference]
  case class Get[Env, SelfReference](callback: Env => SelfReference) extends ReaderF[Env, Knot]
}