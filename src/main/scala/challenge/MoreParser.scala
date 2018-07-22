package challenge

object MoreParser {

  /**
   * Write a parser that will parse zero or more spaces.
   */
  def whitespace: Parser[Unit] =
    ???

  /**
   * Write a function that parses the given string (fails otherwise).
   *
   * Tip: Use `is` and `traverse`.
   */
  def string(s: String): Parser[Unit] =
    ???

  /**
   * Write a function that produces a non-empty list of values coming off the given parser (which must succeed at least
   * once), separated by the second given parser.
   */
  def sepBy1[A, B](ap: Parser[A], bp: Parser[B]): Parser[List[A]] =
    ???

  /**
   * Write a function that produces a list of values coming off the given parser,
   * separated by the second given parser.
   */
  def sepBy[A, B](ap: Parser[A], bp: Parser[B]): Parser[List[A]] =
    ???
}
