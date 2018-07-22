package challenge

import intro._
import challenge.MoreParser._

sealed trait Json
case class JsonString(value: String) extends Json
case class JsonNumber(value: Double) extends Json
case class JsonObject(value: List[(String, Json)]) extends Json
case class JsonArray(value: List[Json]) extends Json
case class JsonBoolean(value: Boolean) extends Json
case object JsonNull extends Json

object JsonParser {

  /**
   * Parse a JSON string. Handle double-quotes, special characters, hexadecimal characters.
   *
   * See http://json.org for the full list of control characters in JSON.
   * Feel free to keep it simple * to start with to pass the basic tests.
   */
  def stringParser: Parser[String] =
    ???

  /**
   * Parse a JSON rational number.
   */
  def numberParser: Parser[Double] =
    ???

  /**
   * Parse a JSON boolean value.
   *
   * Tip: Use `string` and `|||`
   */
  def booleanParser: Parser[Boolean] =
    ???

  /**
   * Parse a JSON `null` literal.
   *
   * Tip: Use `string`
   */
  def nullParser: Parser[Unit] =
    ???

  /**
   * Parse a JSON array.
   *
   * Tip: Use `sepBy` and `jsonParser`
   */
  def arrayParser: Parser[List[Json]] =
    ???

  /**
   * Parse a JSON object.
   *
   * Tip: Use `jsonString`, `sepBy` and `jsonParser`
   */
  def objectParser: Parser[List[(String, Json)]] =
    ???

  /**
   * Parse a JSON value.
   *
   * Tip: Use the other parsers and `|||`
   */
  def jsonParser: Parser[Json] =
    ???

  /**
   * Parse a JSON string.
   *
   * Tip: Use `jsonParser`
   */
  def parse(json: String): Result[Json] =
    ???
}
