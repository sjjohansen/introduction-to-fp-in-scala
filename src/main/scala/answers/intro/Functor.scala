package answers.intro

trait Functor[F[_]] {
  def map[A, B](a: F[A])(f: A => B): F[B]
}

object Functor {

  def apply[F[_]: Functor]: Functor[F] =
    implicitly[Functor[F]]

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

  def void[F[_]: Functor, A](fa: F[A]): F[Unit] =
    Functor[F].map(fa)(_ => ())

  def amap[F[_]: Functor, A](fa: F[A])(a: A): F[A] =
    Functor[F].map(fa)(_ => a)

  def fpair[F[_]: Functor, A](fa: F[A]): F[(A, A)] =
    Functor[F].map(fa)(a => (a, a))

  def fproduct[F[_]: Functor, A, B](fa: F[A])(f: A => B): F[(A, B)] =
    Functor[F].map(fa)(a => (a, f(a)))

  def strengthL[F[_]: Functor, A, B](a: A, f: F[B]): F[(A, B)] =
    Functor[F].map(f)(b => (a, b))

  def strengthR[F[_]: Functor, A, B](f: F[A], b: B): F[(A, B)] =
    Functor[F].map(f)(a => (a, b))
}
