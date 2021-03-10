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

  def get1[Env] : Reader1[Env, Env] = Get1(Done1(_))

  
}