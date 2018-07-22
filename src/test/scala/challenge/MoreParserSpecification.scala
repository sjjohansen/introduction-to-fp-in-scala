package challenge

import intro._
import test._

object MoreParserSpecification extends Properties("MoreParser") {

  property("more parser: whitespace") =
    MoreParser.whitespace.run(" \n\t a") ?= Ok(ParseState("a", ()))

  property("more parser: string") =
    MoreParser.string("test").run("testing") ?= Ok(ParseState("ing", "test"))

  property("more parser: sepBy") =
    MoreParser.sepBy(Parser.natural, Parser.is(' ')).run("1 2 10!") ?=
      Ok(ParseState("!", List(1, 2, 10)))

  property("more parser: sepBy empty") =
    MoreParser.sepBy(Parser.natural, Parser.is(' ')).run("!") ?=
      Ok(ParseState("!", Nil))
}
