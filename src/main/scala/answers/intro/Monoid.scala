package answers.intro

trait Monoid[A] {
  def identity: A
  def op(x: A, y: A): A
}

case class Sum(n: Int)
case class Product(n: Int)
case class Min(n: Int)
case class Max(n: Int)
case class Size(n: Int)
case class Endo[A](f: A => A)
case class First[A](first: Option[A])
case class Last[A](last: Option[A])

object Monoid {

  def apply[A: Monoid]: Monoid[A] =
    implicitly[Monoid[A]]

  implicit def SumMonoid: Monoid[Sum] =
    new Monoid[Sum] {
      def identity: Sum =
        Sum(0)
      def op(x: Sum, y: Sum): Sum =
        Sum(x.n + y.n)
    }

  implicit def ProductMonoid: Monoid[Product] =
    new Monoid[Product] {
      def identity: Product =
        Product(1)
      def op(x: Product, y: Product): Product =
        Product(x.n * y.n)
    }

  implicit def MinMonoid: Monoid[Min] =
    new Monoid[Min] {
      def identity: Min =
        Min(Integer.MAX_VALUE)
      def op(x: Min, y: Min): Min =
        Min(math.min(x.n, y.n))
    }

  implicit def MaxMonoid: Monoid[Max] =
    new Monoid[Max] {
      def identity: Max =
        Max(Integer.MIN_VALUE)
      def op(x: Max, y: Max): Max =
        Max(math.max(x.n, y.n))
    }

  implicit def SizeMonoid: Monoid[Size] =
    ???

  implicit def EndoMonoid[A]: Monoid[Endo[A]] =
    new Monoid[Endo[A]] {
      def identity: Endo[A] =
        Endo[A](a => a)
      def op(x: Endo[A], y: Endo[A]): Endo[A] =
        Endo(a => y.f(x.f(a)))
    }

  implicit def FirstMonoid[A]: Monoid[First[A]] =
    new Monoid[First[A]] {
      def identity: First[A] =
        First[A](None)
      def op(x: First[A], y: First[A]): First[A] =
        First(x.first.orElse(y.first))
    }

  implicit def LastMonoid[A]: Monoid[Last[A]] =
    new Monoid[Last[A]] {
      def identity: Last[A] =
        Last[A](None)
      def op(x: Last[A], y: Last[A]): Last[A] =
        Last(y.last.orElse(x.last))
    }

  implicit def ListMonoid[A]: Monoid[List[A]] =
    new Monoid[List[A]] {
      def identity: List[A] =
        List.empty[A]
      def op(x: List[A], y: List[A]): List[A] =
        x ++ y
    }

  implicit def MapMonoid[A, B: Monoid]: Monoid[Map[A, B]] =
    new Monoid[Map[A, B]] {
      def identity: Map[A, B] =
        Map.empty[A, B]
      def op(x: Map[A, B], y: Map[A, B]): Map[A, B] = {
        (x.keySet ++ y.keySet).map(a => a -> {
          (x.get(a), y.get(a)) match {
            case (None, None) =>
              Monoid[B].identity
            case (Some(b), None) =>
              b
            case (None, Some(b)) =>
              b
            case (Some(b1), Some(b2)) =>
              Monoid[B].op(b1, b2)
          }
        }).toMap
      }
    }

  def foldMap[A, B: Monoid](xs: List[A])(f: A => B): B =
    xs.foldLeft(Monoid[B].identity)((b, a) => Monoid[B].op(b, f(a)))

  def sum[A: Monoid](xs: List[A]): A =
    foldMap(xs)(identity)
}

object MonoidSyntax {

  implicit class AnyMonoidSyntax[A: Monoid](value: A) {
    def |+|(other: A) =
      Monoid[A].op(value, other)
  }
}

object MonoidChallenge {
  import MonoidSyntax._
  import MonoidTupleInstances._

  case class Stats(min: Int, max: Int, total: Int, count: Int, average: Int)
  case class Stock(ticker: String, date: String, cents: Int)

  def compute(data: List[Stock], predicate: Stock => Boolean): Map[String, Stats] =
    ???

  def Data = List(
    Stock("FAKE", "2012-01-01", 10000)
  , Stock("FAKE", "2012-01-02", 10020)
  , Stock("FAKE", "2012-01-03", 10022)
  , Stock("FAKE", "2012-01-04", 10005)
  , Stock("FAKE", "2012-01-05",  9911)
  , Stock("FAKE", "2012-01-06",  6023)
  , Stock("FAKE", "2012-01-07",  7019)
  , Stock("FAKE", "2012-01-08",     0)
  , Stock("FAKE", "2012-01-09",  7020)
  , Stock("FAKE", "2012-01-10",  7020)
  , Stock("CAKE", "2012-01-01",     1)
  , Stock("CAKE", "2012-01-02",     2)
  , Stock("CAKE", "2012-01-03",     3)
  , Stock("CAKE", "2012-01-04",     4)
  , Stock("CAKE", "2012-01-05",     5)
  , Stock("CAKE", "2012-01-06",     6)
  , Stock("CAKE", "2012-01-07",     7)
  , Stock("BAKE", "2012-01-01", 99999)
  , Stock("BAKE", "2012-01-02", 99999)
  , Stock("BAKE", "2012-01-03", 99999)
  , Stock("BAKE", "2012-01-04", 99999)
  , Stock("BAKE", "2012-01-05", 99999)
  , Stock("BAKE", "2012-01-06",     0)
  , Stock("BAKE", "2012-01-07", 99999)
  , Stock("LAKE", "2012-01-01", 10012)
  , Stock("LAKE", "2012-01-02",  7000)
  , Stock("LAKE", "2012-01-03",  1234)
  , Stock("LAKE", "2012-01-04",    10)
  , Stock("LAKE", "2012-01-05",  6000)
  , Stock("LAKE", "2012-01-06",  6099)
  , Stock("LAKE", "2012-01-07",  5999)
  )
}


object MonoidTupleInstances {

  /** A monoid for Tuple2 that merges each element with op */
  implicit def Tuple2Monoid[A: Monoid, B: Monoid]: Monoid[(A, B)] =
    new Monoid[(A, B)] {
      def identity =
        (Monoid[A].identity, Monoid[B].identity)

      def op(x: (A, B), y: (A, B)) =
        (x, y) match {
          case ((a1, b1), (a2, b2)) =>
            (Monoid[A].op(a1, a2), Monoid[B].op(b1, b2))
        }
    }

  /** A monoid for Tuple3 that merges each element with op */
  implicit def Tuple3Monoid[A: Monoid, B: Monoid, C: Monoid]: Monoid[(A, B, C)] =
    new Monoid[(A, B, C)] {
      def identity =
        (Monoid[A].identity, Monoid[B].identity, Monoid[C].identity)

      def op(x: (A, B, C), y: (A, B, C)) =
        (x, y) match {
          case ((a1, b1, c1), (a2, b2, c2)) =>
            (Monoid[A].op(a1, a2), Monoid[B].op(b1, b2), Monoid[C].op(c1, c2))
        }
    }

  /** A monoid for Tuple4 that merges each element with op */
  implicit def Tuple4Monoid[A: Monoid, B: Monoid, C: Monoid, D: Monoid]: Monoid[(A, B, C, D)] =
    new Monoid[(A, B, C, D)] {
      def identity =
        (Monoid[A].identity, Monoid[B].identity, Monoid[C].identity, Monoid[D].identity)

      def op(x: (A, B, C, D), y: (A, B, C, D)) =
        (x, y) match {
          case ((a1, b1, c1, d1), (a2, b2, c2, d2)) =>
            (Monoid[A].op(a1, a2), Monoid[B].op(b1, b2), Monoid[C].op(c1, c2), Monoid[D].op(d1, d2))
        }
    }

  /** A monoid for Tuple5 that merges each element with op */
  implicit def Tuple5Monoid[A: Monoid, B: Monoid, C: Monoid, D: Monoid, E: Monoid]: Monoid[(A, B, C, D, E)] =
    new Monoid[(A, B, C, D, E)] {
      def identity =
        (Monoid[A].identity, Monoid[B].identity, Monoid[C].identity, Monoid[D].identity, Monoid[E].identity)

      def op(x: (A, B, C, D, E), y: (A, B, C, D, E)) =
        (x, y) match {
          case ((a1, b1, c1, d1, e1), (a2, b2, c2, d2, e2)) =>
            (Monoid[A].op(a1, a2), Monoid[B].op(b1, b2), Monoid[C].op(c1, c2), Monoid[D].op(d1, d2), Monoid[E].op(e1, e2))
        }
    }

  /** A monoid for Tuple6 that merges each element with op */
  implicit def Tuple6Monoid[A: Monoid, B: Monoid, C: Monoid, D: Monoid, E: Monoid, F: Monoid]: Monoid[(A, B, C, D, E, F)] =
    new Monoid[(A, B, C, D, E, F)] {
      def identity =
        (Monoid[A].identity, Monoid[B].identity, Monoid[C].identity, Monoid[D].identity, Monoid[E].identity, Monoid[F].identity)

      def op(x: (A, B, C, D, E, F), y: (A, B, C, D, E, F)) =
        (x, y) match {
          case ((a1, b1, c1, d1, e1, f1), (a2, b2, c2, d2, e2, f2)) =>
            (Monoid[A].op(a1, a2), Monoid[B].op(b1, b2), Monoid[C].op(c1, c2), Monoid[D].op(d1, d2), Monoid[E].op(e1, e2), Monoid[F].op(f1, f2))
        }
    }
}
