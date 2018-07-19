package answers.intro

import org.scalacheck._, Arbitrary._, Prop._

object IdSpecification extends Properties("Id") {

  property("id: map") =
    Id(1).map(x => x + 10) ?= Id(11)

  property("id: flatMap") =
    Id(1).flatMap(x => Id(x + 10)) ?= Id(11)
}
