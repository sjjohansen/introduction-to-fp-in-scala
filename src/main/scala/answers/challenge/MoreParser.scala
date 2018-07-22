package answers.challenge

object MoreParser {

  def whitespace: Parser[Unit] =
    Parser.list(Parser.space ||| Parser.is('\n') ||| Parser.is('\t')).map(_ => ())

  def string(s: String): Parser[String] =
    Parser.sequence(s.toList.map(Parser.is)).map(_.mkString)

  def sepBy1[A, B](ap: Parser[A], bp: Parser[B]): Parser[List[A]] =
    for {
      h <- ap
      t <- Parser.list(bp.flatMap(_ => ap))
    } yield h :: t

  def sepBy[A, B](ap: Parser[A], bp: Parser[B]): Parser[List[A]] =
    sepBy1(ap, bp) ||| Parser.value(Nil)
}
