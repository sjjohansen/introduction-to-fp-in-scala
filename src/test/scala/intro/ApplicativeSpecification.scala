package intro

import test._

object ApplicativeSpecification extends Properties("Applicative") {

  property("applicative: id") =
    Applicative[Id].ap(Id(1))(Id((i: Int) => i + 1)) ?= Id(2)

  property("applicative: option") =
    Applicative[Option].ap(Some(1))(Some((i: Int) => i + 1)) ?= Some(2)

  property("applicative: list 1") =
    Applicative[List].ap(List(1, 2, 3))(List((i: Int) => i + 1)) ?= List(2, 3, 4)

  property("applicative: list 2") =
    Applicative[List].ap(List(1, 2, 3))(List((i: Int) => i + 1, (i: Int) => i - 1)) ?= List(2, 3, 4, 0, 1, 2)

  property("applicative: liftA2 some some") =
    Applicative.liftA2(Option(1), Option(2))((i, j) => i + j) ?= Some(3)

  property("applicative: liftA2 some none") =
    Applicative.liftA2(Option(1), None)((i, j) => i + j) ?= None

  property("applicative: liftA3 some some") =
    Applicative.liftA3(Option(1), Option(2), Option(3))((i, j, k) => i + j + k) ?= Some(6)

  property("applicative: liftA3 some none") =
    Applicative.liftA3(Option(1), None, Option(3))((i, j, k) => i + j + k) ?= None

  property("applicative: liftA1 some") =
    Applicative.liftA1(Option(1))(_ + 1) ?= Some(2)

  property("applicative: liftA1 none") =
    Applicative.liftA1(Option.empty[Int])(_ + 1) ?= None

  property("applicative: apRight some") =
    Applicative.apRight(Option(1), Option(2)) ?= Some(2)

  property("applicative: apRight none") =
    Applicative.apRight(Option(1), None) ?= None

  property("applicative: apRight list") =
    Applicative.apRight(List(1, 2), List(3, 4, 5)) ?= List(3, 4, 5, 3, 4, 5)

  property("applicative: apLeft some") =
    Applicative.apLeft(Option(1), Option(2)) ?= Some(1)

  property("applicative: apLeft none") =
    Applicative.apLeft(Option(1), None) ?= None

  property("applicative: apLeft list") =
    Applicative.apLeft(List(1, 2), List(3, 4, 5)) ?= List(1, 1, 1, 2, 2, 2)

  property("applicative: sequence some") =
    Applicative.sequence(List(Option(1), Option(2), Option(3))) ?= Some(List(1, 2, 3))

  property("applicative: sequence none") =
    Applicative.sequence(List(Option(1), None, Option(3))) ?= None

  property("applicative: sequence empty") =
    Applicative.sequence(List[Option[Int]]()) ?= Some(Nil)

  property("applicative: sequence some") =
    Applicative.sequence(List(List(1, 2, 3), List(1, 2))) ?= List(List(1, 1), List(1, 2), List(2, 1), List(2, 2), List(3, 1), List(3, 2))

  property("applicative: sequence some") =
    Applicative.traverse(List(1, 2, 3), (i: Int) => Option(i + 1)) ?= Some(List(2, 3, 4))

  property("applicative: replicateA some") =
    Applicative.replicateA(3, Option(1)) ?= Some(List(1, 1, 1))

  property("applicative: replicateA none") =
    Applicative.replicateA(3, Option.empty[Int]) ?= None

  property("applicative: replicateA list") =
    Applicative.replicateA(2, List(1, 2)) ?= List(List(1, 1), List(1, 2), List(2, 1), List(2, 2))

  property("applicative: filterM some") =
    Applicative.filterM(List(1, 2, 3), (a: Int) => if (a < 9) Some(a % 2 != 0) else None) ?= Some(List(1, 3))

  property("applicative: filterM none") =
    Applicative.filterM(List(1, 2, 3), (a: Int) => if (a < 3) Some(a % 2 != 0) else None) ?= None
}
