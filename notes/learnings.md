# Things to remember

## CheatSheet

1. Use of = in methods definitions. If you return a `Unit` you don't need an
equals sign to the method body:

```scala
def sideOne(s: String) {
    println(s)
}

def sideOneExpl(s: String): Unit = {
    println(s)
}
```

However leaving it out will swallow the return value:

```scala
def ret(s: String): Int = {
    s.length
}

def retInf(s: String) {
    s.length
}
```

Note the return type of `refInf()`:

```
scala> val x = retInf("blah")
x: Unit = ()

scala>
```

2. Implicit functions need the implicit argument marked as so:

```scala
object Obj {
    def method2(x: Int)(implicit q: List[Int]): Long = x + q.length
}

// compiler error without the implicit:
// could not find implicit value for parameter q: List[Int]
implicit val l = (1 to 5).toList

val r = Obj.method2(5)
```

3. Concise example of call-by-value vs call-by-name.

```scala
def something(): Int = {
    println("side effect")
    0
}

def callByValue(x: Int) = {
  println("x1=" + x)
  println("x2=" + x)
}

def callByName(x: => Int) = {
  println("x1=" + x)
  println("x2=" + x)
}

println("calling callByValue()")
callByValue(something())
println("calling callByName()")
callByName(something())
```

Also example of referential transparency (replacing an int value with a thing that returns an int value)

## Optional

1. A sealed trait means we can use pattern matching to handle Full and Empty cases and know we are being exhaustive.

```scala
def fold[X](
  full: A => X,
  empty: => X
): X = this match {
  case Full(a) => full(a)
  case Empty() => empty
}
```

Also note that in the _real_ `Option` class this is implemented with parameter lists.
Unlike a sequence we simply return a default value if the `Option` is `None`

```scala
@inline final def fold[B](ifEmpty: => B)(f: A => B): B =
    if (isEmpty) ifEmpty else f(this.get)
```

2. Right associative operators end with a `:`. Kind of remembered that from somewhere.

```scala
case class MinusEr(i: Int) {
    def :->(a: Int): Int = i - a
    def <-:(a: Int): Int = a - i
}

val m = MinusEr(5)
```

Session:

```scala
scala> m :-> 2
res0: Int = 3

scala> m <-: 7
<console>:13: error: value <-: is not a member of Int
       m <-: 7
         ^

scala> 7 <-: m
res3: Int = 2

scala>
```

## Lists

1. You can do nested extractions in pattern matching. My original version of `ranges` looked like this:

```scala
def ranges(xs: List[Int]): List[(Int, Int)] =
    xs.foldRight(List[(Int, Int)]()) { (elem, acc) =>
      acc match {
        case Nil => List[(Int, Int)]((elem, elem))
        case h :: tail =>
          if (h._1 == elem + 1)
            (elem, h._2) :: tail
          else
            (elem, elem) :: acc
      }
    }
```

But that tuple can be decomposed further:

```scala
def ranges(xs: List[Int]): List[(Int, Int)] =
    xs.foldRight(List[(Int, Int)]()) { (elem, acc) =>
      acc match {
        case Nil => List[(Int, Int)]((elem, elem))
        case (p, m) :: tail =>
          if (p == elem + 1)
            (elem, m) :: tail
          else
            (elem, elem) :: acc
      }
    }
```
