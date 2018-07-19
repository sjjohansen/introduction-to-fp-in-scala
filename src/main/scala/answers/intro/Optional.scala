package answers.intro

sealed trait Optional[A] {

  def fold[X](
    full: A => X,
    empty: => X
  ): X =
    this match {
      case Empty() =>
        empty
      case Full(a) =>
        full(a)
    }

  def map[B](f: A => B): Optional[B] =
    this match {
      case Empty() =>
        Empty()
      case Full(a) =>
        Full(f(a))
    }

  def flatMap[B](f: A => Optional[B]): Optional[B] =
    this match {
      case Empty() =>
        Empty()
      case Full(a) =>
        f(a)
    }

  /** Advanced: using fold */
  def flatMapX[B](f: A => Optional[B]): Optional[B] =
    fold(f, Empty())

  def getOrElse(otherwise: => A): A =
    this match {
      case Empty() =>
        otherwise
      case Full(a) =>
        a
    }

  def |||(alternative: => Optional[A]): Optional[A] =
    this match {
      case Empty() =>
        alternative
      case Full(a) =>
        Full(a)
    }
}

case class Full[A](a: A) extends Optional[A]
case class Empty[A]() extends Optional[A]

object Optional {
  def empty[A]: Optional[A] =
    Empty()

  def ok[A](value: A): Optional[A] =
    Full(value)
}
