package intro

import test._

object MonadSpecification extends Properties("Monad") {

  property("monad: ap some some") =
    Monad.ap(Option(1))(Some((i: Int) => i + 1)) ?= Some(2)

  property("monad: ap some empty") =
    Monad.ap(Option(1))(Option.empty[Int => Int]) ?= None

  property("monad: ap empty full") =
    Monad.ap(Option.empty[Int])(Some((i: Int) => i + 1)) ?= None

  property("monad: join full empty") =
    Monad.join(Option(Option.empty[Int])) ?= None

  property("monad: join full full") =
    Monad.join(Option(Option(1))) ?= Some(1)

  property("monad: andThen") =
    Monad.andThen((i: Int) => Option(i + 1), (j: Int) => Option(j * 2)).apply(4) ?= Some(10)
}
