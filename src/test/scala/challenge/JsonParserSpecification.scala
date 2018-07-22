package challenge

import intro._
import test._

object JsonParserSpecification extends Properties("JsonParser") {

  property("json parser: ok no whitespace") =
    JsonParser.parse(
      """ { "key1":true,"key2":[7,false],"key3":{"key4":null}} """
    ) ?= Ok(JsonObject(List(
      "key1" -> JsonBoolean(true)
      , "key2" -> JsonArray(List(JsonNumber(7), JsonBoolean(false)))
      , "key3" -> JsonObject(List("key4" -> JsonNull))
    )))

  property("json parser: ok") =
    JsonParser.parse(
    """ { "key1" : true , "key2" : [7, false] , "key3" : { "key4" : null } } """
    ) ?= Ok(JsonObject(List(
        "key1" -> JsonBoolean(true)
      , "key2" -> JsonArray(List(JsonNumber(7), JsonBoolean(false)))
      , "key3" -> JsonObject(List("key4" -> JsonNull))
      )))
}
