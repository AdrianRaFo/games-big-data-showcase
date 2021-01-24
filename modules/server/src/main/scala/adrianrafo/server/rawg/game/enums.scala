package adrianrafo.server.rawg.game

import enumeratum.EnumEntry._
import enumeratum._

sealed trait EsrbRating extends Hyphencase

object EsrbRating extends Enum[EsrbRating] with CirceEnum[EsrbRating] with DoobieEnum[EsrbRating] {

  case object Everyone extends EsrbRating

  case object Everyone10Plus extends EsrbRating

  case object Teen extends EsrbRating

  case object Mature extends EsrbRating

  case object AdultsOnly extends EsrbRating

  case object RatingPending extends EsrbRating

  val values = findValues
}

sealed trait RatingSlug extends EnumEntry with Lowercase

object RatingSlug extends Enum[RatingSlug] with CirceEnum[RatingSlug] with DoobieEnum[RatingSlug] {

  case object Exceptional extends RatingSlug

  case object Recommended extends RatingSlug

  case object Meh extends RatingSlug

  case object Skip extends RatingSlug

  val values = findValues
}
