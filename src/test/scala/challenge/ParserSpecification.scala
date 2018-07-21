package challenge

import intro._
import test._

object ParserSpecification extends Properties("Parser") {

  property("parser: ok") =
    Parser.value(5).run("hello") ?= Ok(ParseState("hello", 5))

  property("parser: fail") =
    Parser.failed[Unit](NotEnoughInput).run("hello") ?= Fail(NotEnoughInput)

  property("parser: map ok") =
    Parser.value(5).map(x => x + 7).run("hello") ?= Ok(ParseState("hello", 12))

  property("parser: map fail") =
    Parser.failed[Int](NotEnoughInput).map(x => x + 7).run("hello") ?= Fail(NotEnoughInput)

  property("parser: flatMap ok") =
    Parser.value(5).flatMap(x => Parser.value(x + 7)).run("hello") ?= Ok(ParseState("hello", 12))

  property("parser: flatMap fail") =
    Parser.failed[Int](NotEnoughInput).flatMap(x => Parser.value(x + 7)).run("hello") ?= Fail(NotEnoughInput)

  property("parser: >>> ok") =
    (Parser.value(5) >>> Parser.value(7)).run("hello") ?= Ok(ParseState("hello", 7))

  property("parser: >>> fail") =
    (Parser.failed(NotEnoughInput) >>> Parser.value(7)).run("hello") ?= Fail(NotEnoughInput)

  property("parser: ||| ok") =
    (Parser.value(5) ||| Parser.value(7)).run("hello") ?= Ok(ParseState("hello", 5))

  property("parser: ||| fail") =
    (Parser.failed[Int](NotEnoughInput) ||| Parser.value(7)).run("hello") ?= Ok(ParseState("hello", 7))

  property("parser: character") =
    Parser.character.run("hello") ?= Ok(ParseState("ello", 'h'))

  property("parser: list") =
    Parser.list(Parser.character).run("hello") ?= Ok(ParseState("", List('h', 'e', 'l', 'l', 'o')))

  property("parser: list empty") =
    Parser.list(Parser.character).run("") ?= Ok(ParseState("", Nil))

  property("parser: list1") =
    Parser.list1(Parser.character).run("hello") ?= Ok(ParseState("", List('h', 'e', 'l', 'l', 'o')))

  property("parser: list1 empty") =
    Parser.list1(Parser.character).run("") ?= Fail(NotEnoughInput)

  property("parser: satisfy") =
    Parser.satisfy(c => c == 'h').run("hello") ?= Ok(ParseState("ello", 'h'))

  property("parser: is") =
    Parser.is('h').run("hello") ?= Ok(ParseState("ello", 'h'))

  property("parser: digit") =
    Parser.digit.run("123hello") ?= Ok(ParseState("23hello", '1'))

  property("parser: digit fail") =
    Parser.digit.run("hello") ?= Fail(UnexpectedInput("h"))

  property("parser: natural") =
    Parser.natural.run("123hello") ?= Ok(ParseState("hello", 123))

  property("parser: natural fail") =
    Parser.natural.run("hello") ?= Fail(UnexpectedInput("h"))

  property("parser: space") =
    Parser.space.run(" hello") ?= Ok(ParseState("hello", ' '))

  property("parser: space fail") =
    Parser.space.run("hello") ?= Fail(UnexpectedInput("h"))

  property("parser: spaces1") =
    Parser.spaces1.run("    hello") ?= Ok(ParseState("hello", "    "))

  property("parser: spaces1 fail") =
    Parser.spaces1.run("hello") ?= Fail(UnexpectedInput("h"))

  property("parser: lower") =
    Parser.lower.run("hello") ?= Ok(ParseState("ello", 'h'))

  property("parser: lower fail") =
    Parser.lower.run("Hello") ?= Fail(UnexpectedInput("H"))

  property("parser: upper") =
    Parser.upper.run("Hello") ?= Ok(ParseState("ello", 'H'))

  property("parser: upper fail") =
    Parser.upper.run("hello") ?= Fail(UnexpectedInput("h"))

  property("parser: alpha") =
    Parser.alpha.run("hello") ?= Ok(ParseState("ello", 'h'))

  property("parser: alpha fail") =
    Parser.alpha.run("?hello") ?= Fail(UnexpectedInput("?"))

  property("parser: sequence") =
    Parser.sequence(List(Parser.character, Parser.character, Parser.character)).run("hello") ?= Ok(ParseState("lo", List('h', 'e', 'l')))

  property("parser: sequence fail") =
    Parser.sequence(List(Parser.character, Parser.failed[Char](NotEnoughInput), Parser.character)).run("hello") ?= Fail(NotEnoughInput)

  property("parser: thisMany") =
    Parser.thisMany(5, Parser.character).run("hello") ?= Ok(ParseState("", List('h', 'e', 'l', 'l', 'o')))

  property("parser: thisMany fail") =
    Parser.thisMany(6, Parser.character).run("hello") ?= Fail(NotEnoughInput)

  property("person") = {
    import PersonParser._
    PersonParser.parseAll(PersonParser.Data) ?= Ok(List(
        Person("Fred", 32, "123.456-1213#", Address(301, "cobblestone"))
      , Person("Barney", 31, "123.456.1214#", Address(303, "cobblestone"))
      , Person("Homer", 39, "555.123.939#", Address(742, "evergreen"))
      , Person("Flanders", 39, "555.123.939#", Address(744, "evergreen"))
    ))
  }
}
