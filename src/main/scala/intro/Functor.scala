package intro

/**
 * Abstraction of types that can be mapped over.
 *
 * The following laws should hold:
 *
 *  1) map(r)(z => z) == r
 *  2) map(r)(z => f(g(z))) == map(map(r)(g))(f)
 */
trait Functor[F[_]] {

  def map[A, B](a: F[A])(f: A => B): F[B]
}

object Functor {
  /**
   * Convenience for summoning a Functor instance.
   *
   * usage: Functor[Int].map(1, 2)
   */
  def apply[F[_]: Functor]: Functor[F] =
    implicitly[Functor[F]]

  /* Functor Instances (cheating is good) */

  implicit def IdFunctor: Functor[Id] = new Functor[Id] {
    def map[A, B](a: Id[A])(f: A => B): Id[B] =
      a.map(f)
  }

  implicit def OptionFunctor: Functor[Option] = new Functor[Option] {
    def map[A, B](a: Option[A])(f: A => B): Option[B] =
      a.map(f)
  }

  implicit def OptionalFunctor: Functor[Optional] = new Functor[Optional] {
    def map[A, B](a: Optional[A])(f: A => B): Optional[B] =
      a.map(f)
  }

  implicit def ListFunctor: Functor[List] = new Functor[List] {
    def map[A, B](a: List[A])(f: A => B): List[B] =
      Lists.map(a)(f)
  }

  /* Functor Library */

  /**
   * Anonymous map producing unit value.
   *
   * scala> Functor[Optional].void(Full(1))
   * resX: Optional[Unit] = Full(())
   *
   * scala> Functor[Optional].void(Empty)
   * resX: Optional[Unit] = Empty
   *
   * scala> Functor[List].void(List(1, 2, 3))
   * resX: List[Unit] = List((), (), ())
   */
  def void[F[_]: Functor, A](fa: F[A]): F[Unit] =
    ???

  /**
   * Anonymous map. Maps a constant value on a functor.
   *
   * scala> Functor[Optional].amap(Full(1))(2)
   * resX: Optional[Int] = Full(2)
   *
   * scala> Functor[Optional].amap(Empty)(2)
   * resX: Optional[Int] = Empty
   *
   * scala> Functor[List].amap(List(1, 2, 3))(7)
   * resX: List[Int] = List(7, 7, 7)
   */
  def amap[F[_]: Functor, A](fa: F[A])(a: A): F[A] =
    ???

  /** Exercise: Twin all `A`s in `fa`. */
  def fpair[F[_]: Functor, A](fa: F[A]): F[(A, A)] =
    ???

  /** Exercise: Pair all `A`s in `fa` with the result of function application. */
  def fproduct[F[_]: Functor, A, B](fa: F[A])(f: A => B): F[(A, B)] =
    ???

  /** Exercise: Inject `a` to the left of `B`s in `f`. */
  def strengthL[F[_]: Functor, A, B](a: A, f: F[B]): F[(A, B)] =
    ???

  /** Exercise: Inject `b` to the right of `A`s in `f`. */
  def strengthR[F[_]: Functor, A, B](f: F[A], b: B): F[(A, B)] =
    ???
}
