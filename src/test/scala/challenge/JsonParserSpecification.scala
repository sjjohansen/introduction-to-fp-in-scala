package answers.challenge

import answers.intro._
import test._

object JsonParserSpecification extends Properties("JsonParser") {

  property("json parser: string") =
    JsonParser.stringParser.parseAll("\" abc\"") ?= Ok(" abc")

  property("json parser: number natural") =
    JsonParser.numberParser.parseAll("123") ?= Ok(123)

  property("json parser: number decimal") =
    JsonParser.numberParser.parseAll("123.4") ?= Ok(123.4)

  property("json parser: number negative") =
    JsonParser.numberParser.parseAll("-123") ?= Ok(-123)

  property("json parser: boolean true") =
    JsonParser.booleanParser.parseAll("true") ?= Ok(true)

  property("json parser: boolean false") =
    JsonParser.booleanParser.parseAll("false") ?= Ok(false)

  property("json parser: null") =
    JsonParser.nullParser.parseAll("null") ?= Ok(())

  property("json parser: array") =
    JsonParser.arrayParser.parseAll("""["1", 2, true]""") ?=
      Ok(List(JsonString("1"),JsonNumber(2),JsonBoolean(true)))

  property("json parser: array empty") =
    JsonParser.arrayParser.parseAll("[]") ?= Ok(Nil)

  property("json parser: object") =
    JsonParser.objectParser.parseAll("""{"a":"x","b":2,"c":true}""") ?=
      Ok(List("a" -> JsonString("x"), "b" -> JsonNumber(2), "c" -> JsonBoolean(true)))

  property("json parser: object empty") =
    JsonParser.objectParser.parseAll("{}") ?= Ok(Nil)

  property("json parser: ok no whitespace") =
    JsonParser.jsonParser.parseAll(
      """{ "key1":true,"key2":[7,false],"key3":{"key4":null}}"""
    ) ?= Ok(JsonObject(List(
        "key1" -> JsonBoolean(true)
      , "key2" -> JsonArray(List(JsonNumber(7), JsonBoolean(false)))
      , "key3" -> JsonObject(List("key4" -> JsonNull))
    )))

  property("json parser: ok") =
    JsonParser.jsonParser.parseAll(
    """ { "key1" : true , "key2" : [7, false] , "key3" : { "key4" : null } } """
    ) ?= Ok(JsonObject(List(
        "key1" -> JsonBoolean(true)
      , "key2" -> JsonArray(List(JsonNumber(7), JsonBoolean(false)))
      , "key3" -> JsonObject(List("key4" -> JsonNull))
      )))
}
