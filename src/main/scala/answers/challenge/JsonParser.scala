package answers.challenge

import answers.intro._
import answers.challenge.MoreParser._

sealed trait Json
case class JsonString(value: String) extends Json
case class JsonNumber(value: Double) extends Json
case class JsonObject(value: List[(String, Json)]) extends Json
case class JsonArray(value: List[Json]) extends Json
case class JsonBoolean(value: Boolean) extends Json
case object JsonNull extends Json

/**
 * FIX: This should implement the full JSON spec, it's a quick hack at the moment
 */
object JsonParser {

  def stringParser: Parser[String] =
    for {
      _ <- Parser.is('"')
      s <- Parser.list(Parser.satisfy(_ != '"'))
      _ <- Parser.is('"')
    } yield s.mkString

  def numberParser: Parser[Double] =
    for {
      m <- string("-") ||| string("")
      n <- Parser.natural.map(_.toString)
      d <- Parser.is('.').flatMap(p => Parser.natural.map(n => p + n.toString)) ||| string("")
    } yield (m + n + d).toDouble

  def booleanParser: Parser[Boolean] =
    string("true").map(_ => true) ||| string("false").map(_ => false)

  def nullParser: Parser[Unit] =
    string("null").map(_ => ())

  def arrayParser: Parser[List[Json]] = {
    for {
      _ <- Parser.is('[')
      _ <- whitespace
      s <- sepBy(for {
        _ <- whitespace
        j <-jsonParser
        _ <- whitespace
        } yield j, Parser.is(','))
      _ <- whitespace
      _ <- Parser.is(']')
    } yield s
  }

  def objectParser: Parser[List[(String, Json)]] = {
    val jp = for {
      _ <- whitespace
      k <- stringParser
      _ <- whitespace
      _ <- Parser.is(':')
      _ <- whitespace
      v <- jsonParser
      _ <- whitespace
    } yield k -> v

    for {
      _ <- whitespace
      _ <- Parser.is('{')
      s <- sepBy(jp, Parser.is(','))
      _ <- Parser.is('}')
      _ <- whitespace
    } yield s
  }

  def jsonParser: Parser[Json] =
    stringParser.map(s => JsonString(s): Json) |||
    numberParser.map(JsonNumber) |||
    booleanParser.map(JsonBoolean) |||
    nullParser.map(_ => JsonNull) |||
    arrayParser.map(JsonArray) |||
    objectParser.map(JsonObject)
}
