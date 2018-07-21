package answers.intro

trait Applicative[F[_]] {

  def point[A](a: => A): F[A]

  def ap[A, B](a: F[A])(f: F[A => B]): F[B]
}

object Applicative {

  def apply[F[_]: Applicative]: Applicative[F] =
    implicitly[Applicative[F]]

  implicit def IdApplicative: Applicative[Id] =
    new Applicative[Id] {
      def point[A](a: => A): Id[A] =
        Id(a)
      def ap[A, B](a: Id[A])(f: Id[A => B]): Id[B] =
        Id(f.value(a.value))
    }

  implicit def OptionApplicative: Applicative[Option] =
    new Applicative[Option] {
      def point[A](a: => A): Option[A] =
        Some(a)
      def ap[A, B](a: Option[A])(f: Option[A => B]): Option[B] =
        a match {
          case None =>
            None
          case Some(b) =>
            f match {
              case None =>
                None
              case Some(fb) =>
                Some(fb(b))
            }
        }
    }

  implicit def ListApplicative: Applicative[List] =
    new Applicative[List] {
      def point[A](a: => A): List[A] =
        List(a)
      def ap[A, B](a: List[A])(f: List[A => B]): List[B] =
        for {
          fb <- f
          b <- a
        } yield fb(b)
    }

  def liftA2[F[_]: Applicative, A, B, C](a: F[A], b: F[B])(f: (A, B) => C): F[C] = {
    val A = Applicative[F]
    A.ap(b)(A.ap(a)(A.point(f.curried)))
  }

  def liftA3[F[_]: Applicative, A, B, C, D](a: F[A], b: F[B], c: F[C])(f: (A, B, C) => D): F[D] = {
    val A = Applicative[F]
    // Or you can re-use liftA2
    // A.ap(c)(liftA2(a, b)((a2, b2) => f.curried(a2)(b2)))
    A.ap(c)(A.ap(b)(A.ap(a)(A.point(f.curried))))
  }

  def liftA1[F[_]: Applicative, A, B](a: F[A])(f: A => B): F[B] = {
    val A = Applicative[F]
    A.ap(a)(A.point(f))
  }

  def apRight[F[_]: Applicative, A, B](a: F[A], b: F[B]): F[B] =
    liftA2(a, b)((_, b2) => b2)

  def apLeft[F[_]: Applicative, A, B](a: F[A], b: F[B]): F[A] =
    liftA2(a, b)((a2, _) => a2)

  def sequence[F[_]: Applicative, A](xs: List[F[A]]): F[List[A]] =
    xs match {
      case Nil =>
        Applicative[F].point(Nil)
      case h :: t =>
        liftA2(h, sequence(t))((h2, t2) => h2 :: t2)
    }

  def traverse[F[_]: Applicative, A, B](xs: List[A], f: A => F[B]): F[List[B]] =
    sequence(xs.map(f))

  def replicateA[F[_]: Applicative, A](n: Int, xs: F[A]): F[List[A]] =
    sequence(List.fill(n)(xs))

  def filterM[F[_]: Applicative, A](xs: List[A], p: A => F[Boolean]): F[List[A]] =
    Applicative.liftA1(
      sequence(xs.map(a => Applicative.liftA1(p(a))(r => if (r) Some(a) else None)))
    )(_.flatMap(_.toList))
}
