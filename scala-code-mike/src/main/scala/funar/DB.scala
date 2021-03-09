package funar

/*
put("Mike", 15)
x = get("Mike")
put("Mike", x + 1)
y = get("Mike")
return y
*/
object DB {
  /*
  trait DBCommand
  case class Put(key: String, value: Int) extends DBCommand
  case class Get(key: String) extends DBCommand

  type DBProgram = List[DBCommand]

  val p1 = List(Put("Mike", 15), Get("Mike"))
  */

  type Key = String
  type Value = Int

  // "Datenbank-Programm mit Resultat vom Typ A"
  trait DB[A] {
    def flatMap[B](next: A => DB[B]): DB[B] = splice(this)(next)
    def     map[B](   f: A =>    B): DB[B] =
      this match {
        case Get(key, callback) =>
          Get(key, value => callback(value).map(f))
        case Put(key, value, callback) =>
          Put(key, value, _ => callback(()).map(f))
        case Return(result) => Return(f(result))
      }
  }
  case class Get[A](key: Key, callback: Value => DB[A]) extends DB[A]
  case class Put[A](key: Key, value: Value, callback: Unit => DB[A]) extends DB[A]
  case class Return[A](result: A) extends DB[A]

  val p1 =
    Put("Mike", 15, (_) =>
    Get("Mike", x =>
    Put("Mike", x + 1, (_) =>
    Get("Mike", y =>
    Return(y)))))

  def get(key: Key): DB[Value] =
    Get(key, value => Return(value))
  
  def put(key: Key, value: Value): DB[Unit] =
    Put(key, value, Return(_)) // unit => Return(unit)

  // splice(dbA, Return) ~~ dbA
  // splice(Return(x), next) ~~ next(x)
  // splice(splice(dBA, nextB), nextC) ~~~
  //   splice(dbA, a => splice(nextB(a), nextC))
  def splice[A, B](dbA: DB[A])(next: A => DB[B]): DB[B] =
    dbA match {
      case Get(key, callback) => 
        Get(key, value => splice(callback(value))(next))
      case Put(key, value, callback) =>
        Put(key, value, _ => splice(callback(()))(next))
      case Return(result) => next(result)
    }

  // DB + flatMap / splice + Return = Monade

  val p1_a =
    splice(put("Mike", 15))((_) =>
    splice(get("Mike"))(x =>
    splice(put("Mike", x+1))((_) =>
    splice(get("Mike"))(y =>
    Return(y)))))

  val p1_b =
    for {
      _ <- put("Mike", 15)
      x <- get("Mike")
      _ <- put("Mike", x+1)
      y <- get("Mike")
    } yield y
  
}