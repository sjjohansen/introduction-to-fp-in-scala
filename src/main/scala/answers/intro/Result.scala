package answers.intro

sealed trait Error
case class NotANumber(s: String) extends Error
case class InvalidOperation(s: String) extends Error
case class UnexpectedInput(s: String) extends Error
case object NotEnoughInput extends Error

case class Fail[A](error: Error) extends Result[A]
case class Ok[A](value: A) extends Result[A]

sealed trait Result[A] {

  def fold[X](
    fail: Error => X,
    ok: A => X
  ): X =
    this match {
      case Fail(e) =>
        fail(e)
      case Ok(a) =>
        ok(a)
    }

  def flatMap[B](f: A => Result[B]): Result[B] =
    this match {
      case Fail(e) =>
        Fail(e)
      case Ok(a) =>
        f(a)
    }

  /** Advanced: using fold */
  def flatMapX[B](f: A => Result[B]): Result[B] =
    fold(Fail(_), f)

  def map[B](f: A => B): Result[B] =
    this match {
      case Fail(e) =>
        Fail(e)
      case Ok(a) =>
        Ok(f(a))
    }

  /** Advanced: using flatMap */
  def mapX[B](f: A => B): Result[B] =
    flatMap(a => Ok(f(a)))

  def getOrElse(otherwise: => A): A =
    this match {
      case Fail(_) =>
        otherwise
      case Ok(a) =>
        a
    }

  def |||(alternative: => Result[A]): Result[A] =
    this match {
      case Fail(_) =>
        alternative
      case Ok(a) =>
        Ok(a)
    }
}

object Result {
  def notANumber[A](s: String): Result[A] =
    fail(NotANumber(s))

  def unexpectedInput[A](s: String): Result[A] =
    fail(UnexpectedInput(s))

  def notEnoughInput[A]: Result[A] =
    fail(NotEnoughInput)

  def ok[A](value: A): Result[A] =
    Ok(value)

  def fail[A](error: Error): Result[A] =
    Fail(error)

  def sequence[A](xs: List[Result[A]]): Result[List[A]] =
    xs match {
      case Nil =>
        Ok(Nil)
      case h :: t =>
        h.flatMap(a => sequence(t).map(xt => a :: xt))
    }
}


object ResultExample {

  /** Simplified calculation data type. */
  sealed trait Operation
  case object Plus extends Operation
  case object Minus extends Operation
  case object Multiply extends Operation

  def int(body: String): Result[Int] =
    ???

  def operation(op: String): Result[Operation] =
    ???

  def calculate(op: Operation, n: Int, m: Int): Int =
    ???

  def attempt(op: String, n: String, m: String): Result[Int] =
    ???

  def run(args: List[String]): Result[Int] =
    ???

  def main(args: Array[String]) =
    println(run(args.toList) match {
      case Ok(result) => s"result: ${result}"
      case Fail(error) => s"failed: ${error}"
    })
}
