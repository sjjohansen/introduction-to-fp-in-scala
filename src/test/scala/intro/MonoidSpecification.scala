package intro

import Monoid._
import test._

object MonoidSpecification extends Properties("Monoid") {

  property("monoid: sum") =
    monoidLaws(Monoid.SumMonoid, arbitrary[Int].map(Sum))

  property("monoid: product") =
    monoidLaws(Monoid.ProductMonoid, arbitrary[Int].map(Product))

  property("monoid: min") =
    monoidLaws(Monoid.MinMonoid, arbitrary[Int].map(Min))

  property("monoid: max") =
    monoidLaws(Monoid.MaxMonoid, arbitrary[Int].map(Max))

  property("monoid: first") =
    monoidLaws(Monoid.FirstMonoid[Int], arbitrary[Option[Int]].map(First(_)))

  property("monoid: last") =
    monoidLaws(Monoid.LastMonoid[Int], arbitrary[Option[Int]].map(Last(_)))

  property("monoid: list") =
    monoidLaws(Monoid.ListMonoid[Int], arbitrary[List[Int]])

  property("monoid: map") =
    monoidLaws(Monoid.MapMonoid[Int, List[Int]], arbitrary[Map[Int, List[Int]]])

  property("monoid: foldMap sum") =
    Monoid.foldMap(List(1, 2, 3, 4, 5))(x => Sum(x)) ?= Sum(15)

  property("monoid: foldMap list") =
    Monoid.foldMap(List(1, 2, 3, 4, 5))(x => List(x)) ?= List(1, 2, 3, 4, 5)

  property("monoid: sum") =
    Monoid.sum(List(1, 2, 3, 4, 5).map(Sum)) ?= Sum(15)

  def monoidLaws[A](m: Monoid[A], gen: Gen[A]): Prop =
    new Properties("MonoidLaws") {

      property("left identity") =
        forAll(gen) { a =>
          m.op(m.identity, a) ?= a
        }

      property("right identity") =
        forAll(gen) { a =>
          m.op(a, m.identity) ?= a
        }

      property("associativity") =
        forAll(gen, gen, gen) { (a1, a2, a3) =>
          m.op(a1, m.op(a2, a3)) ?= m.op(m.op(a1, a2), a3)
        }
    }
}
