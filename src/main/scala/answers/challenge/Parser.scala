package answers.challenge

import answers.intro._

case class ParseState[A](input: String, value: A)

case class Parser[A](run: String => Result[ParseState[A]]) {

  def map[B](f: A => B): Parser[B] =
    Parser(s => run(s).map(ps =>
      ps.copy(value = f(ps.value))
    ))

  def flatMap[B](f: A => Parser[B]): Parser[B] =
    Parser(s => run(s).flatMap(ps =>
      f(ps.value).run(ps.input)
    ))

  def >>>[B](parser: => Parser[B]): Parser[B] =
    flatMap(_ => parser)

  def |||(f: => Parser[A]): Parser[A] =
    Parser(s => run(s) ||| f.run(s))

  def parseAll(value: String): Result[A] =
    run(value).flatMap(s =>
      if (s.input.isEmpty) Ok(s.value) else Fail(UnexpectedInput(s.input))
    )
}

object Parser {

  def value[A](a: A): Parser[A] =
    Parser(s => Ok(ParseState(s, a)))

  def failed[A](error: Error): Parser[A] =
    Parser(_ => Fail(error))

  def character: Parser[Char] =
    Parser(s => s
      .headOption
      .map(c => Ok(ParseState(s.drop(1), c)))
      .getOrElse(Fail(NotEnoughInput))
    )

  def list[A](parser: Parser[A]): Parser[List[A]] =
    list1(parser) ||| value(Nil)

  def list1[A](parser: Parser[A]): Parser[List[A]] =
    for {
      h <- parser
      t <- list(parser)
    } yield h :: t

  def satisfy(pred: Char =>  Boolean): Parser[Char] =
    character.flatMap(c =>
      if (pred(c)) value(c) else failed(UnexpectedInput(c.toString))
    )

  def is(char: Char): Parser[Char] =
    satisfy(_ == char)

  def digit: Parser[Char] =
    satisfy(Character.isDigit)

  def natural: Parser[Int] =
    list1(digit).map(_.mkString.toInt)

  def space: Parser[Char] =
    is(' ')

  def spaces1: Parser[String] =
    list1(space).map(_.mkString)

  def lower: Parser[Char] =
    satisfy(Character.isLowerCase)

  def upper: Parser[Char] =
    satisfy(Character.isUpperCase)

  def alpha: Parser[Char] =
    satisfy(Character.isLetterOrDigit)

  def sequence[A](parsers: List[Parser[A]]): Parser[List[A]] =
    parsers match {
      case Nil =>
        Parser.value(Nil)
      case h :: t =>
        h.flatMap(a => sequence(t).map(xt => a :: xt))
    }

  def thisMany[A](n: Int, parse: Parser[A]): Parser[List[A]] =
    sequence(List.fill(n)(parse))
}

object PersonParser {

  case class Person(name: String, age: Int, phone: String, address: Address)

  case class Address(number: Int, street: String)

  def nameParser: Parser[String] =
    for {
      h <- Parser.upper
      t <- Parser.list(Parser.lower)
    } yield (h :: t).mkString

  def phoneParser: Parser[String] =
    for {
      h <- Parser.digit
      t <- Parser.list(Parser.digit ||| Parser.is('.') ||| Parser.is('-'))
      e <- Parser.is('#')
    } yield (h :: t ++ List(e)).mkString

  /*
     * An address is a positive street number and a non empty string for the
   * street name.
   */
  def addressParser: Parser[Address] =
    for {
      n <- Parser.natural
      _ <- Parser.space
      s <- Parser.list1(Parser.lower).map(_.mkString)
    } yield Address(n, s)

  def personParser: Parser[Person] =
    for {
      nm <- nameParser
      _ <- Parser.space
      ag <- Parser.natural
      _ <- Parser.space
      ph <- phoneParser
      _ <- Parser.space
      ad <- addressParser
    } yield Person(nm, ag, ph, ad)

  def parseAll(data: List[String]): Result[List[Person]] =
    Result.sequence(data.map(s => personParser.parseAll(s)))

  def Data = List(
    "Fred 32 123.456-1213# 301 cobblestone"
  , "Barney 31 123.456.1214# 303 cobblestone"
  , "Homer 39 555.123.939# 742 evergreen"
  , "Flanders 39 555.123.939# 744 evergreen"
  )
}
