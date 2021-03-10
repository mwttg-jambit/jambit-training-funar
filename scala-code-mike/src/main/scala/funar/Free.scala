package funar

import cats._
import cats.implicits._ 
import cats.data._ 

object Free {
  // Reader, aber diesmal als Datentyp, unabhängig von der Implementierung
  // wie DB
  sealed trait Reader1[Env, A]
  case class Get1[Env, A](callback: Env => Reader1[Env, A]) extends Reader[Env, A] 
}