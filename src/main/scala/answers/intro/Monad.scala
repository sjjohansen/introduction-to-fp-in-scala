package answers.intro

trait Monad[F[_]] {

  def point[A](a: => A): F[A]

  def bind[A, B](a: F[A])(f: A => F[B]): F[B]
}

object Monad {

  def apply[F[_]: Monad]: Monad[F] =
    implicitly[Monad[F]]

  implicit def IdMonad: Monad[Id] =
    new Monad[Id] {
      def point[A](a: => A): Id[A] =
        Id(a)
      def bind[A, B](a: Id[A])(f: A => Id[B]): Id[B] =
        a.flatMap(f)
    }

  implicit def OptionMonad: Monad[Option] =
    new Monad[Option] {
      def point[A](a: => A): Option[A] =
        Some(a)
      def bind[A, B](a: Option[A])(f: A => Option[B]): Option[B] =
        a.flatMap(f)
    }

  implicit def ListMonad: Monad[List] =
    new Monad[List] {
      def point[A](a: => A): List[A] =
        List(a)
      def bind[A, B](a: List[A])(f: A => List[B]): List[B] =
        a.flatMap(f)
    }

  def map[F[_]: Monad, A, B](a: F[A])(f: A => B): F[B] = {
    val M = Monad[F]
    M.bind(a)(b => M.point(f(b)))
  }

  def ap[F[_]: Monad, A, B](fa: F[A])(fab: F[A => B]): F[B] =
    Monad[F].bind(fa)(a => Monad.map(fab)(ab => ab(a)))

  def join[F[_]: Monad, A](fa: F[F[A]]): F[A] =
    Monad[F].bind(fa)(identity)

  def andThen[F[_]: Monad, A, B, C](afb: A => F[B], bfc: B => F[C]): A => F[C] =
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
