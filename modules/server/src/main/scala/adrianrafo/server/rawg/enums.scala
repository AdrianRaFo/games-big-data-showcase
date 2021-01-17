package adrianrafo.server.rawg

import enumeratum.EnumEntry._
import enumeratum._

sealed trait EsrbSlug extends Hyphencase

object EsrbSlug extends Enum[EsrbSlug] with CirceEnum[EsrbSlug] {

  case object Everyone extends EsrbSlug

  case object Everyone10Plus extends EsrbSlug

  case object Teen extends EsrbSlug

  case object Mature extends EsrbSlug

  case object AdultsOnly extends EsrbSlug

  case object RatingPending extends EsrbSlug

  val values = findValues
}

sealed trait RatingSlug extends EnumEntry with Lowercase

object RatingSlug extends Enum[RatingSlug] with CirceEnum[RatingSlug] {

  case object Exceptional extends RatingSlug

  case object Recommended extends RatingSlug

  case object Meh extends RatingSlug

  case object Skip extends RatingSlug

  val values = findValues
}
