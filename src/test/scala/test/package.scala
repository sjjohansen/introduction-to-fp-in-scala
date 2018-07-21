import org.scalacheck._
import org.scalacheck.util.Pretty

package object test {

  type Properties = org.scalacheck.Properties
  type Prop = org.scalacheck.Prop
  type Gen[A] = org.scalacheck.Gen[A]

  implicit def AnyOperators[T](x: => T)(implicit pretty: T => Pretty): ExtendedAny[T] =
    new ExtendedAny[T](x)

  def arbitrary[T](implicit a: Arbitrary[T]): Gen[T] =
    Arbitrary.arbitrary

  def forAll[T1, P](g1: Gen[T1])
    (f: T1 => P)
    (implicit p: P => Prop, s1: Shrink[T1], pp1: T1 => Pretty): Prop =
    Prop.forAll(g1)(f)

  def forAll[T1, T2, P](g1: Gen[T1], g2: Gen[T2])
    (f: (T1, T2) => P)
    (implicit p: P => Prop, s1: Shrink[T1], pp1: T1 => Pretty, pp2: T2 => Pretty): Prop =
    Prop.forAll(g1, g2)(f)

  def forAll[T1, T2, T3, P](g1: Gen[T1], g2: Gen[T2], g3: Gen[T3])
    (f: (T1, T2, T3) => P)
    (implicit p: P => Prop, s1: Shrink[T1], pp1: T1 => Pretty, pp2: T2 => Pretty, pp3: T3 => Pretty): Prop =
    Prop.forAll(g1, g2, g3)(f)
}

class ExtendedAny[T](x: => T)(implicit pretty: T => Pretty) {

  def ?=(y: T): Prop =
    try {
      Prop.?=(x, y)
    } catch {
      case e: NotImplementedError =>
        val t = e.getStackTrace()(1)
        Prop.falsified.label(s"Not implemented: ${t.getClassName}.${t.getMethodName}")
    }
}

