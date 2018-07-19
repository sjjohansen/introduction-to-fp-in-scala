package answers.intro

object Lists {

  def length[A](xs: List[A]): Int =
    xs match {
      case Nil =>
        0
      case _ :: t =>
        1 + length(t)
    }

  def lengthX[A](xs: List[A]): Int =
    xs.foldRight(0)((_, c) => c + 1)

  def append[A](x: List[A], y: List[A]): List[A] =
    x match {
      case Nil =>
        y
      case h :: t =>
        h :: append(t, y)
    }

  def map[A, B](xs: List[A])(f: A => B): List[B] =
    xs match {
      case Nil =>
        Nil
      case h :: t =>
        f(h) :: map(t)(f)
    }

  def filter[A](xs: List[A])(p: A => Boolean): List[A] =
    xs match {
      case Nil =>
        Nil
      case h :: t =>
        if (p(h)) {
          h :: filter(t)(p)
        } else {
          filter(t)(p)
        }
    }

  def reverse[A](xs: List[A]): List[A] = {
    @annotation.tailrec
    def go(ys: List[A], zs: List[A]): List[A] =
      ys match {
        case Nil =>
          zs
        case h :: t =>
          go(t, h :: zs)
      }
    go(xs, Nil)
  }

  def sequence[A](xs: List[Option[A]]): Option[List[A]] =
    xs match {
      case Nil =>
        Some(Nil)
      case h :: t =>
        h.flatMap(a => sequence(t).map(xt => a :: xt))
    }

  def ranges(xs: List[Int]): List[(Int, Int)] = {
    @annotation.tailrec
    def go(ys: List[Int], zs: List[(Int, Int)]): List[(Int, Int)] =
      (ys, zs) match {
        case (Nil, _) =>
          reverse(zs)
        case (x :: t, Nil) =>
          go(t, (x, x) :: Nil)
        case (x :: t, (y1, y2) :: t2) =>
          if (x == y2 + 1) {
            go(t, (y1, x) :: t2)
          } else {
            go(t, (x, x) :: (y1, y2) :: t2)
          }
      }
    go(xs, Nil)
  }
}
