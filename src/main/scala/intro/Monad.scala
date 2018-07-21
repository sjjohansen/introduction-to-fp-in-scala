package intro

trait Monad[F[_]] {
  def point[A](a: => A): F[A]
  def pure[A](a: => A): F[A] = point(a)

  def bind[A, B](a: F[A])(f: A => F[B]): F[B]

  def map[A, B](a: F[A])(f: A => B): F[B] =
    bind(a)(b => point(f(b)))
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
      def point[A](a: => A): Id[A] =
        ???
      def bind[A, B](a: Id[A])(f: A => Id[B]): Id[B] =
        ???
    }

  /**
   * Binds a function on an Optional.
   */
  implicit def OptionMonad: Monad[Option] =
    new Monad[Option] {
      def point[A](a: => A): Option[A] =
        ???
      def bind[A, B](a: Option[A])(f: A => Option[B]): Option[B] =
        ???
    }

  /**
   * Binds a function on a List.
   *
   * scala> List(1, 2, 3).flatMap(n => List(n, n))
   * resX: List[Int] = List(1, 1, 2, 2, 3, 3)
   */
  implicit def ListMonad: Monad[List] =
    new Monad[List] {
      def point[A](a: => A): List[A] =
        ???
      def bind[A, B](a: List[A])(f: A => List[B]): List[B] =
        ???
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
    ???

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
    ???

  /**
   * Implement composition within the `Monad`` environment.
   *
   * scala> Monad[Option].andThen((i: Int) => Some(i + 1), (j: Int) => Some(j * 2)).apply(4)
   * resX: Option[Int] = Some(10)
   */
  def andThen[F[_]: Monad, A, B, C](afb: A => F[B], bfc: B => F[C]): A => F[C] =
    ???
}

object MonadSyntax {

  implicit class AnyMonadSyntax[M[_]: Monad, A](a: M[A]) {

    def map[B](f: A => B): M[B] =
      Monad[M].map(a)(f)

    def flatMap[B](f: A => M[B]): M[B] =
      Monad[M].bind(a)(f)
  }
}
