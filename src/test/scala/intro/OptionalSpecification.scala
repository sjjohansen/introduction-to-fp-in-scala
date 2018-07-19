package intro

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop.forAll
import test._

object OptionalSpecification extends Properties("Optional") {

  property("map: empty") =
    Optional.empty[Int].map(_ + 1) ?= Optional.empty

  property("map: full") =
    forAll(arbitrary[Int]) { i =>
      Full(i).map(_ + 1) ?= Full(i + 1)
    }

  property("flatMap: empty") =
    Optional.empty[Int].flatMap(Optional.ok) ?= Optional.empty

  property("flatMap: even dec, odd inc, even input") =
    Full(8).flatMap(n => if (n % 2 == 0) Full(n - 1) else  Full(n + 1)) ?= Full(7)

  property("flatMap: even dec, odd inc, odd input") =
    Full(9).flatMap(n => if (n % 2 == 0) Full(n - 1) else  Full(n + 1)) ?= Full(10)

  property("getOrElse: full") =
    Full(8).getOrElse(99) ?= 8

  property("getOrElse: empty") =
    Optional.empty[Int].getOrElse(99) ?= 99

  property("getOrElse: first full") =
    Full(8) ||| Optional.empty ?= Full(8)

  property("getOrElse: both full") =
    Full(8) ||| Full(9) ?= Full(8)

  property("getOrElse: first empty") =
    Optional.empty[Int] ||| Full(9) ?= Full(9)

  property("getOrElse: both empty") =
    Optional.empty[Int] ||| Optional.empty ?= Optional.empty
}
