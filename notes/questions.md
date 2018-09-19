# My Qs

## General

Q: It seems like this local `sbt` wrapper script is fairly common?
* https://github.com/paulp/sbt-extras
* Also in Play and Akka

A:

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
A:

## Id

Q: What is the purpose of the `Id.point()` method?
A:

Q: Why use ScalaCheck for testing vs something like ScalaTest?
   https://www.scalacheck.org/
A:

## Optional

Q: Is the `|||` choice operator some kind of standard or imported from somewhere else (Haskell?)
A:    
