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

// note we can also use a def that returns List[Int]
/*
implicit def l: List[Int] = (1 to 5).toList
*/

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

4. Algebraic Data Types (ADT): https://alvinalexander.com/scala/fp-book/algebraic-data-types-adts-in-scala



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

## Result

1. `flatMap` and bind (in Haskell this is `>>=`):

https://stackoverflow.com/questions/29187983/comparing-haskell-and-scala-bind-flatmap-examples

2. I actually find this one (which is correct):

```scala
def flatMap[B](f: A => Result[B]): Result[B] = fold(Fail(_), f)
```

harder to read than this one (probably due to types and underscores):

```scala
def flatMap[B](f: A => Result[B]): Result[B] = this.fold(e => Fail(e), f)
```

2. Scala streams unsafe GC problems

## Functor

1. WTF is a Functor and a Monad (with sugar & peanuts):

https://medium.com/beingprofessional/understanding-functor-and-monad-with-a-bag-of-peanuts-8fa702b3f69e

Functor appears to be a thing with a `map` and an `apply` (called unit/return elsewhere)

The definition of `identity`: https://github.com/scala/scala/blob/2.13.x/src/library/scala/Predef.scala#L206

```scala
@inline def identity[A](x: A): A = x // see `$conforms` for the implicit version
```

The `@inline` annotation: https://www.scala-lang.org/api/2.12.3/scala/inline.html
Will try really had to have the compiler inline methods, which is basically like a macro
expansion at compile time. The idea is to reduce the number of frames in the call stack.

When we pass `identity` to a functor it proves that we only run the function `identity` and cause no other side effects or state changes.  This is the _identity law_.

Functors also obey the _associative law_ which basically says if we call `map(f)` and then `map(g)` that should be the same as calling `map(x => g(f(x)))`, or `g` composed with `f`.

Concrete examples of functors are `Option`, `List`, `Future` and `Try`.

They allow us to do things like this, where we don't have to have `if-else` logic to handle the difference between `Some` & `None`:

```scala
amm> Some(1).map(_ + 2).map(_ * 3)
res9: Option[Int] = Some(9)

amm> (None: Option[Int]).map(_ + 2).map(_ * 3)
res10: Option[Int] = None
```

2. Type-class implicit (type class pattern)
The idea is to provide an alternative to inheritance via a sort of implicit adapter pattern.
(see https://docs.scala-lang.org/tutorials/FAQ/context-bounds.html)

```scala
// why does this work?

def apply[F[_]: Functor]: Functor[F] =
  implicitly[Functor[F]]
```

Turns out it could be rewritten as this:

```scala
def apply[F[_]](implicit fun: Functor[F]): Functor[F] = fun
```

So what basically happens is `implicitly` and an implicit parameter group to `apply`.
Here's the definition of from [Predef](https://github.com/scala/scala/blob/2.13.x/src/library/scala/Predef.scala#L214):

```scala
@inline def implicitly[T](implicit e: T) = e
```

So in our case that is the bit I've added as `(implicit fun: Functor[F])`.
The other part is this `[F[_]: Functor]`.
This is one of those _higher-kinded types_ where we expect one "hole" to be filled by the type we select. So `Int` & `Map[String,Int]` are no good but `List[Int]` is fine.
The purpose of the colon in the type definition is to signify a context bound. This means that if we have this:

```scala
def f[A: B](a: A)...
```

Somewhere we also need an `implicit` `B[A]`.

So to tie it all together, calling this:

```scala
Functor[Id].map(Id(5))(identity)
```

* This calls `Functor.apply[Id]` which has no explicit arguments.
* As `Id` has a single type hole, it is ok for `F[_]`.
* The `implicitly[Functor[F]]` adds the equivalent of `(implicit fun: Functor[F])` to `apply`.
* This will check the scope for an implicit definition of `Functor[Id]`.
* That finds `implicit def IdFunctor: Functor[Id] ...` as a `def` that returns the correct type can be implicit.
* That `IdFunctor` method creates an anonymous class that mixes in the `Functor` trait and defines `map`
* The `apply` method then returns that new anonymous class
* We then call `map` on that and away we go!
