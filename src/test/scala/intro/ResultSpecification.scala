package answers.intro

import org.scalacheck._, Arbitrary._, Prop._

object ResultSpecification extends Properties("Result") {

  property("Result#fold 1") =
    Ok(1).fold(_ => 0, x => x) ?= 1

  property("Result#fold 2") =
    Fail[Int](NotEnoughInput).fold(_ => 0, x => x) ?= 0

  property("Result#flatMap 1") =
    Ok(1).flatMap(x => Ok(x + 10)) ?= Ok(11)

  property("Result#flatMap 2") =
    Ok(1).flatMap(x => Fail[Int](NotEnoughInput)) ?= Fail(NotEnoughInput)

  property("Result#flatMap 3") =
   Fail[Int](NotEnoughInput).flatMap(x => Ok(x + 10)) ?= Fail(NotEnoughInput)

  property("Result#flatMap 4") =
    Fail[Int](NotEnoughInput).flatMap(x => Fail[Int](UnexpectedInput("?"))) ?= Fail(NotEnoughInput)

  property("Result#map 1") =
    Ok(1).map(x => x + 10) ?= Ok(11)

  property("Result#map 2") =
    Fail[Int](NotEnoughInput).map(x => x + 10) ?= Fail(NotEnoughInput)

  property("Result#getOrElse 1") =
    Ok(1).getOrElse(10) ?= 1

  property("Result#getOrElse 2") =
    Fail(NotEnoughInput).getOrElse(10) ?= 10

  property("Result#||| 1") =
    Ok(1) ||| Ok(10) ?= Ok(1)

  property("Result#||| 2") =
    Ok(1) ||| Fail[Int](NotEnoughInput) ?= Ok(1)

  property("Result#||| 3") =
    Fail[Int](NotEnoughInput) ||| Ok(10) ?= Ok(10)

  property("Result#||| 4") =
    Fail[Int](NotEnoughInput) ||| Fail[Int](UnexpectedInput("?")) ?= Fail[Int](UnexpectedInput("?"))

  property("Result#sequence ok") =
    Lists.sequence(List[Result[Int]](Ok(1), Ok(2), Ok(3))) ?= Ok(List(1, 2, 3))

  property("Result#sequence fail") =
    Lists.sequence(List[Result[Int]](Ok(1), Fail(NotEnoughInput), Ok(3))) ?= Fail(NotEnoughInput)
}
