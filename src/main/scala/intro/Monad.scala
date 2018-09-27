package intro

import scala.annotation.tailrec

trait Monad[F[_]] {

  def point[A](a: => A): F[A]

  //  flatMap?
  def bind[A, B](a: F[A])(f: A => F[B]): F[B]
}

object Monad {

  /**
   * Convenience for summoning a Monad instance.
   *
   * usage: Monad[Int].bind(List(1))(i => List(i - 1, i + 1))
   */
  def apply[F[_]: Monad]: Monad[F] =
    implicitly[Monad[F]]

  /**
   * Binds a function on the Id monad.
   */
  implicit def IdMonad: Monad[Id] =
    new Monad[Id] {
      def point[A](a: => A): Id[A] = Id(a)
      def bind[A, B](a: Id[A])(f: A => Id[B]): Id[B] = f(a.value)
    }

  /**
   * Binds a function on an Optional.
   */
  implicit def OptionMonad: Monad[Option] =
    new Monad[Option] {
      def point[A](a: => A): Option[A] = Option(a)
      def bind[A, B](a: Option[A])(f: A => Option[B]): Option[B] = a match {
        case None => None: Option[B]
        case Some(a) => f(a)
      }
    }

  /**
   * Binds a function on a List.
   *
   * scala> List(1, 2, 3).flatMap(n => List(n, n))
   * resX: List[Int] = List(1, 1, 2, 2, 3, 3)
   */
  implicit def ListMonad: Monad[List] =
    new Monad[List] {
      def point[A](a: => A): List[A] = List(a)

      def bind[A, B](a: List[A])(f: A => List[B]): List[B] = {

        @tailrec
        def innerBind(acc: List[B], l: List[A], fun: A => List[B]): List[B] = l match {
          case Nil => List[B]()
          case h :: tail => innerBind(f(h) ++ acc, tail, f)
        }

        innerBind(List[B](), a, f)
      }
    }

  /**
   * Witness that all things with `bind` and `point` also have `map`
   *
   * scala> Monad[Option].map(Option(1))((i: Int) => i + 1)
   * resX: Option[Int] = Some(2)
   */
  def map[F[_]: Monad, A, B](a: F[A])(f: A => B): F[B] = {
    Monad[F].bind(a) { x => Monad[F].point(f(x)) }
  }

  /**
   * Witness that all things with `bind` and `map` also have `ap`
   *
   * scala> Monad[Option].ap(Option(1))(Some((i: Int) => i + 1)
   * resX: Option[Int] = Some(2)
   *
   * scala> Monad[Option].ap(Option(1))(Option.empty[Int => Int])
   * resX: Option[Int] = None
   *
   * scala> Monad[Option].ap(Option.none[Int])(Some((i: Int) => i + 1)
   * resX: Option[Int] = None
   */
  def ap[F[_]: Monad, A, B](fa: F[A])(fab: F[A => B]): F[B] =
    Monad[F].bind(fa) { a => Monad.map(fab)(ab => ab(a)) }

  /**
   * Flattens a combined structure to a single structure.
   *
   * scala> Monad[Option].ap(Option(Option.empty[Int]))
   * resX: Option[Int] = None
   *
   * scala> Monad[Option].ap(Option(Option(1)))
   * resX: Option[Int] = Some(1)
   */
  def join[F[_]: Monad, A](fa: F[F[A]]): F[A] =
    //Monad[F].bind(fa)(x => x)
  /*
   * from the point of view of bind:
   * A: is Option[Option[Int]]
   * B: Int
   *
   * so bind returns Option[Int]
   * so bind unwraps one level of fa, giving us Option[Int]
   * we then run identity on it, returning Option[Int]
   *
   * from the point of view of join:
   * A: is Int
   * F: is Option
   * so we need to return Option[Int]
   */
    Monad[F].bind(fa)(identity)

  /**
   * Implement composition within the `Monad`` environment.
   *
   * scala> Monad[Option].andThen((i: Int) => Some(i + 1), (j: Int) => Some(j * 2)).apply(4)
   * resX: Option[Int] = Some(10)
   */
  def andThen[F[_]: Monad, A, B, C](afb: A => F[B], bfc: B => F[C]): A => F[C] =
  /*
   * our example function afb is Int => Option[Int]
   * A: Int
   * B: Int
   * F[B]: Option[Int]
   *
   * our example function bfc is Int => Option[Int]
   * B: Int
   * C: Int
   * F[C]: Option[Int]
   *
   * we need to return Int => Option[Int]
   *
   * we call bind with:
   * A: Option[Int]
   * B: Int
   * return: Option[Int]
   */
    a => Monad[F].bind(afb(a))(bfc)
}

object MonadSyntax {

  implicit class AnyMonadSyntax[M[_]: Monad, A](a: M[A]) {

    def map[B](f: A => B): M[B] =
      Monad.map(a)(f)

    def flatMap[B](f: A => M[B]): M[B] =
      Monad[M].bind(a)(f)
  }
}
