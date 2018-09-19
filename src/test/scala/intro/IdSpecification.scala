package intro

import test._

object IdSpecification extends Properties("Id") {

  property("id: map") =
    Id(1).map(x => x + 10) ?= Id(11)

  val g: Int => Int = _ + 2
  val f: Int => Int = _ * 5

  property("id: map#nested") =
    Id(1).map(x => f(g(x))) ?= Id(15)

  property("id: flatMap") =
    Id(1).flatMap(x => Id(x + 10)) ?= Id(11)

  property("id: point") =
    Id.point(9) ?= Id(9)
}
