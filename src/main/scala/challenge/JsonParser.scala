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
   *
   * scala> JsonParser.stringParser.parseAll("\"abc\"")
   *  = Ok(abc)
   *
   * scala> JsonParser.stringParser.parseAll("\" abc\"")
   *  = Ok( abc)
   */
  def stringParser: Parser[String] =
    ???

  /**
   * Parse a JSON rational number.
   *
   * Feel free to start by parsing natural numbers (int), and move on to the rest of the challenge.
   *
   * scala> JsonParser.numberParser.parseAll("123")
   *  = Ok(123)
   *
   * scala> JsonParser.numberParser.parseAll("123.4")
   *  = Ok(123.4)
   *
   * scala> JsonParser.numberParser.parseAll("-123")
   *  = Ok(-123)
   */
  def numberParser: Parser[Double] =
    ???

  /**
   * Parse a JSON boolean value.
   *
   * Tip: Use `string` and `|||`
   *
   * scala> JsonParser.booleanParser.parseAll("true")
   *  = Ok(true)
   *
   * scala> JsonParser.booleanParser.parseAll("false")
   *  = Ok(false)
   */
  def booleanParser: Parser[Boolean] =
    ???

  /**
   * Parse a JSON `null` literal.
   *
   * Tip: Use `string`
   *
   * scala> JsonParser.nullParser.parseAll("null")
   *  = Ok(())
   */
  def nullParser: Parser[Unit] =
    ???

  /**
   * Parse a JSON array.
   *
   * Tip: Use `sepBy` and `jsonParser`
   *
   * scala> JsonParser.arrayParser.parseAll("""["1", 2, true]""")
   *  = Ok(List(JsonString(1),JsonNumber(2),JsonBoolean(true)))
   *
   * scala> JsonParser.arrayParser.parseAll("[]")
   *  = Ok(List())
   */
  def arrayParser: Parser[List[Json]] =
    ???

  /**
   * Parse a JSON object.
   *
   * Tip: Use `jsonString`, `sepBy` and `jsonParser`
   *
   * scala> JsonParser.objectParser.parseAll("""{"a":"x","b":2,"c":true}""")
   *  = Ok(List((a,JsonString("x")),(b,JsonNumber(2)),(c,JsonBoolean(true))))
   *
   * scala> JsonParser.objectParser.parseAll("")
   *  = Ok(List())
   *
   */
  def objectParser: Parser[List[(String, Json)]] =
    ???

  /**
   * Parse a JSON value.
   *
   * Tip: Use the other parsers and `|||`
   *
   * scala> JsonParser.jsonParser.parseAll("""{"key1":true}""")
   *  = Ok(JsonObject(List((key1,JsonBoolean(true))))
   *
   * scala> JsonParser.jsonParser.parseAll("""{"key1":true,"key2":[7,false]}""")
   *  = Ok(JsonObject(List((key1,JsonBoolean(true)),(key2,JsonArray(List(JsonNumber(7), JsonBoolean(false)))))))
   *
   * scala> JsonParser.jsonParser.parseAll("""{"key1":true,"key2":[7,false],"key3":{"key4":null}}""")
   *  = Ok(JsonObject(List((key1,JsonBoolean(true)),(key2,JsonArray(List(JsonNumber(7), JsonBoolean(false)))),(key3,JsonObject(List((key4,JsonNull))))
   */
  def jsonParser: Parser[Json] =
    ???
}
