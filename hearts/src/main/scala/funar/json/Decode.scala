package funar.json

import io.circe._
import cats._
import cats.implicits._
import cats.data._
import scala.annotation.tailrec

object Decode {
  import Json._

  sealed trait Error
  object Error {
    case class Field(name: String, inner: Error) extends Error
    case class Index(index: Int, inner: Error) extends Error
    case class OneOf(inners: Seq[Error]) extends Error
    case class Failure(message: String, value: Json) extends Error
  }
  import Error._

  type Decoder[A] = Json => Either[Error, A]

  def string: Decoder[String] = { json =>
    json.asString.map(Right(_)).getOrElse(Left(Failure("not a string", json)))
  }

  def int: Decoder[Int] = { json =>
    json.asNumber.flatMap(_.toInt).map(Right(_)).getOrElse(Left(Failure("not an int", json)))
  }

  def double: Decoder[Double] = { json =>
    json.asNumber.map(_.toDouble).map(Right(_)).getOrElse(Left(Failure("not a double", json)))
  }

  def boolean: Decoder[Boolean] = { json =>
    json.asBoolean.map(Right(_)).getOrElse(Left(Failure("not a boolean", json)))
  }

  def list[A](element: Decoder[A]): Decoder[List[A]] = { json =>
    json.asArray match {
      case Some(vs) =>
        vs.toList.traverse(element)
      case None => Left(Failure("not a json array", json))
    }
  }

  def index[A](i: Int, element: Decoder[A]): Decoder[A] = { json =>
    json.asArray match {
      case Some(vs) =>
        if (i < vs.length)
          element(vs(i)).swap.map(Index(i, _)).swap
        else
          Left(Failure("index out of bounds: " + i, json))
      case None =>
          Left(Failure("not an array (index " + i + ")", json))
    }
  }

  def oneOf[A](alternatives: Decoder[A]*): Decoder[A] = { Json =>
    val results = alternatives.toIterable.map(_(Json))
    results.collectFirst { case result@Right(value) => result } match {
      case Some(result) => result
      case None => Left(Error.OneOf(results.map(_.swap.getOrElse(???)).toSeq))
    }
  }

  implicit val decoderFunctor: Functor[Decoder] = new Functor[Decoder] {
    def map[A, B](decoder: Decoder[A])(f: A => B): Decoder[B] = json => decoder(json).map(f)
  }

  implicit val decoderApplicative: Applicative[Decoder] = new Applicative[Decoder] {
    def pure[A](value: A) = json => Right(value)
    def ap[A, B](ff : Decoder[A=>B])(fa: Decoder[A]): Decoder[B] = ???

  }

  implicit val decoderMonad: Monad[Decoder] = new Monad[Decoder] {
    def flatMap[A, B](decoder: Decoder[A])(f: A => Decoder[B]): Decoder[B] = json => decoder(json).flatMap(a => f(a)(json))
    def pure[A](value: A) = json => Right(value)

    def tailRecM[A, B](a: A)(f: A => Decoder[Either[A, B]]): Decoder[B] = { Json =>
      @tailrec def loop(a: A): Either[Error, B] =
        f(a)(Json) match {
          case Left(error) => Left(error)
          case Right(Left(nextA)) => loop(nextA)
          case Right(Right(b)) => Right(b)
        }
      loop(a)
    }
  }

  def nulld[A](value: A): Decoder[A] = { json =>
    if (json.isNull)
      Right(value)
    else
      Left(Failure("not null", json))
  }

  def succeed[A](value: A): Decoder[A] = { json => Right(value) }

  def fail[A](message: String): Decoder[A] = { json => Left(Failure(message, json)) }

  def optional[A](decoder: Decoder[A]): Decoder[Option[A]] =
    oneOf(decoder.map(Some(_)), succeed(None))


  def field[A](name: String, decoder: Decoder[A]): Decoder[A] = { json =>
    json.asObject match {
      case Some(obj) =>
        obj(name) match {
          case None => Left(Failure("field " + name + " not found", json))
          case Some(entry) => decoder(entry).swap.map(Field(name, _)).swap
        }
      case _ => Left(Failure("not an object", json))
    }
  }


}
