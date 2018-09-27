# Fixes

1. Typo in comment `Emptyy()`:

https://github.com/charleso/introduction-to-fp-in-scala/blob/master/src/main/scala/intro/Optional.scala#L46

2. Misleading comment `Functor[Int].map(1, 2)`:

https://github.com/charleso/introduction-to-fp-in-scala/blob/master/src/main/scala/intro/Functor.scala#L20

3. Pure isn't mentioned anywhere but used in this comment. Probably should be point:

https://github.com/charleso/introduction-to-fp-in-scala/blob/master/src/main/scala/intro/Applicative.scala#L15

4. The return type in `apLeft` seems wrong:

https://github.com/charleso/introduction-to-fp-in-scala/blob/master/src/main/scala/intro/Applicative.scala#L128

In the answers:

https://github.com/charleso/introduction-to-fp-in-scala/blob/master/src/main/scala/answers/intro/Applicative.scala#L72

Pretty sure this should be `F[A]`

5. Comment should probably read `Applicative.traverse` and the answer should be `Some(List(2,3,4))`

https://github.com/charleso/introduction-to-fp-in-scala/blob/master/src/main/scala/intro/Applicative.scala#L152

6. Shows examples for Monad.ap, not Monad.join:

https://github.com/charleso/introduction-to-fp-in-scala/blob/master/src/main/scala/intro/Monad.scala#L83


