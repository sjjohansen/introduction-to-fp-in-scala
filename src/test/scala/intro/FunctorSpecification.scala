package intro

import test._

object FunctorSpecification extends Properties("Functor") {

  property("functor: void full") =
    Functor.void(Optional.ok(2)) ?= Full(())

  property("functor: void empty") =
    Functor.void(Optional.empty[Int]) ?= Empty()

  property("functor: void list") =
    Functor.void(List(1, 2, 3)) ?= List((), (), ())

  property("functor: amap full") =
    Functor.amap(Optional.ok(2))(3) ?= Full(3)

  property("functor: amap empty") =
    Functor.amap(Optional.empty[Int])(3) ?= Empty()

  property("functor: amap list") =
    Functor.amap(List(1, 2, 3))(7) ?= List(7, 7, 7)

  property("functor: fpair") =
    Functor.fpair(Id(2)) ?= Id((2,2))

  property("functor: fproduct") =
    Functor.fproduct(Id(2))(_ + 1) ?= Id((2,3))

  property("functor: strengthL") =
    Functor.strengthL(1, Id(2)) ?= Id((1,2))

  property("functor: strengthR") =
    Functor.strengthR(Id(1), 2) ?= Id((1,2))
}
