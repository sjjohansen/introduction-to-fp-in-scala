package intro

import intro.ResultExample._
import test._

object ResultExampleSpecification extends Properties("ResultExample") {

  property("ResultExample#int 1") =
    ResultExample.int("100") ?= Ok(100)

  property("ResultExample#int 2") = {
    val body = "100.1"
    ResultExample.int(body) ?= Fail(NotANumber(body))
  }

  property("ResultExample#operation 1") =
    ResultExample.operation("+") ?= Ok(Plus)

  property("ResultExample#operation 2") =
    ResultExample.operation("-") ?= Ok(Minus)

  property("ResultExample#operation 3") =
    ResultExample.operation("*") ?= Ok(Multiply)

  property("ResultExample#operation 4") = {
    val body = "/"
    ResultExample.operation(body) ?= Fail(InvalidOperation(body))
  }

  property("ResultExample#calculate 1") =
    ResultExample.calculate(Plus, 7, 9) ?= 16

  property("ResultExample#calculate 2") =
    ResultExample.calculate(Minus, 7, 9) ?= -2

  property("ResultExample#calculate 3") =
    ResultExample.calculate(Multiply, 7, 9) ?= 63

  property("ResultExample#attempt 1") =
    ResultExample.attempt("*", "7", "9") ?= Ok(63)

  property("ResultExample#attempt 2") =
    ResultExample.attempt("*", "7.1", "9") ?= Fail(NotANumber("7.1"))

  property("ResultExample#attempt 3") =
    ResultExample.attempt("/", "7", "9") ?= Fail(InvalidOperation("/"))

}
