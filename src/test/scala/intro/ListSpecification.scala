package intro

import org.scalacheck._, Arbitrary._, Prop._

object ListSpecification extends Properties("List") {

  property("Lists#length") =
    Lists.length(List(1, 2, 3, 4)) ?= 4

  property("Lists#lengthX") =
    Lists.lengthX(List(1, 2, 3, 4)) ?= 4

  property("Lists#append") =
    Lists.append(List(1, 2, 3, 4), List(5, 6, 7, 8)) ?=  List(1, 2, 3, 4, 5, 6, 7, 8)

  property("Lists#append prop") =
    forAll(arbitrary[List[Int]], arbitrary[List[Int]], arbitrary[List[Int]]) { (x, y, z) =>
      Lists.append(Lists.append(x, y), z) ?= Lists.append(x, Lists.append(y, z))
    }

  property("Lists#map") =
    Lists.map(List(1, 2, 3, 4))(x => x + 1) ?= List(2, 3, 4, 5)

  property("Lists#filter") =
    Lists.filter(List(1, 2, 3, 4))(i => i < 3) ?= List(1, 2)

  property("Lists#filter true") =
    forAll(arbitrary[List[Boolean]]) { l =>
      Lists.filter(l)(_ => true) ?= l
    }

  property("Lists#filter false") =
    forAll(arbitrary[List[Boolean]]) { l =>
      Lists.filter(l)(_ => false) ?= Nil
    }

  property("Lists#reverse") =
    Lists.reverse(List(1, 2, 3, 4)) ?= List(4, 3, 2, 1)

  property("Lists#reverse p1") =
    forAll(arbitrary[List[Int]], arbitrary[List[Int]]) { (x, y) =>
      Lists.reverse(x) ++ Lists.reverse(y) ?= Lists.reverse(y ++ x)
    }

  property("Lists#reverse p2") =
    forAll(arbitrary[Int]) { x =>
      Lists.reverse(x :: Nil) ?= x :: Nil
    }

  property("Lists#sequence some") =
    Lists.sequence(List[Option[Int]](Some(1), Some(2), Some(3))) ?= Some(List(1, 2, 3))

  property("Lists#sequence none") =
    Lists.sequence(List[Option[Int]](Some(1), None, Some(3))) ?= None

  property("Lists#ranges 1") =
    Lists.ranges(List(1, 2, 3, 4, 7, 8, 9, 10, 30, 40, 41)) ?=  List((1, 4), (7, 10), (30, 30), (40, 41))

  property("Lists#ranges 2") =
    Lists.ranges(List(1, 2, 3, 4)) ?= List((1, 4))

  property("Lists#ranges 3") =
    Lists.ranges(List(1, 2, 4)) ?= List((1, 2), (4, 4))
}
