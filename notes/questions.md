# My Qs

## General

Q: It seems like this local `sbt` wrapper script is fairly common?
* https://github.com/paulp/sbt-extras
* Also in Play and Akka

A: Seems to be a legacy that is almost a common standard in Scala projects.
   An attempt to make

## CheatSheet

Q: What are some solid examples of when `var` is preferred for "performance reasons"
A:

Q: What are Algebraic Data Types (ADT)?
A: Implemented as a sealed trait with all it's subclasses inside one file.
   The subclasses are 'data constructors' for that trait.
   These are called 'sum' or 'sum-types' in scala.
   To explore more: https://alvinalexander.com/scala/fp-book/algebraic-data-types-adts-in-scala

Q: Sealed traits and ADTs?
A: See above on sealed traits.
   This ensures all subclasses are defined at compilation time.


Q: In the example on "higher-kinded polymorphism" why is it `myClass[F[_]]` and not `myClass[F[A]]`?
   If we defined this as say `myClass[List[Int]]` do we lose the type of `List[Int]` ?
A:

Q: What exactly are monads in the term "monad comprehension" in for-yield?
A:

Q: Are type aliases mostly for compile time safety?
   Is the point to ensure that if we have different List[Int] types in the same class we enforce where they are used etc?
A: These are replaced with the actual type at compile time so they are more for the purpose of having readable aliases in the code rather than enforcing types.
   See Value Classes for how this can be done: https://docs.scala-lang.org/overviews/core/value-classes.html

## Id

Q: What is the purpose of the `Id.point()` method?
A: All monads require this.

Q: Why use ScalaCheck for testing vs something like ScalaTest?
   https://www.scalacheck.org/
A: There are nice things about property based testing I need to read up on.

## Optional

Q: Is the `|||` choice operator some kind of standard or imported from somewhere else (Haskell?)
A: Seems to be a Haskell thing

## Lists

Q: Is there a reason to use cons `::` in `length` rather than just matching `Nil` & `_`.
   I would assume `List` is a sealed trait with `Nil` so the default would have to match?
A: Didn't notice the answer was using recursion and foolishly just called List.length() rather than actually implementing anything :unamused:

Q: Had a `foldRight` version:
   `x.foldRight(y)((elem,acc) =>  elem :: acc)`
   Didn't think this was ideal because we end up traversing the first list backwards.
   Used a reverse on `x` in my solution then did a `foldLeft` across that.
   The actual solution uses list decomposition but isn't tail recursive. Won't that eventually blow up the stack?
   Interestingly enough the actual `List.foldRight` implementation uses `reverse anyway`:
   https://github.com/scala/scala/blob/2.13.x/src/library/scala/collection/immutable/List.scala#L317
 A:

 Q: General observation of using non-tailrec vs folds. Need to better understand where to use what.
 A:

 Q: Also @annotation.tailrec vs @tailrec. Do we prefer more qualification for a style reason?
 A:

 Q: The solution for `sequence` would traverse the whole list via recursion and then unwind back until it hit the first `None`. My solution was to try and do list decomposition and then break on the first `None` which I assume is running forward from the start of the list. If it was a big `List` and the `None` was in an early element wouldn't that be more efficient?
 A:

Q: Under the covers it seems like `foldRight` doing it's `reverse` would make it pretty close to how the solution
   for `ranges` has been implemented. My understanding is what `tailrec` unwraps into a `while` loop anyway, which
   is what happens in `reverse` so the result should be the same?
A:

## Result

Q: Need to research this: "For our result type this is just called fold. More formally we refer to this as a catamorphism."
   The first hit on Stackoverflow adds a whole bunch more concepts I don't have :confused: : https://stackoverflow.com/questions/23724220/in-what-way-is-scalas-option-fold-a-catamorphism
A: 
