package adrianrafo.server.rawg

import enumeratum.{CirceEnum, Enum}
import enumeratum.EnumEntry.Hyphencase
import enumeratum.values.StringEnumEntry

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

sealed abstract class EsrbName(val value: String) extends StringEnumEntry

object EsrbName extends Enum[EsrbName] with CirceEnum[EsrbName] {

  case object Everyone extends EsrbName("Everyone")
  case object Everyone10Plus extends EsrbName("Everyone 10+")
  case object Teen extends EsrbName("Teen")
  case object Mature extends EsrbName("Mature")
  case object AdultsOnly extends EsrbName("Adults Only")
  case object RatingPending extends EsrbName("Rating Pending")

  val values = findValues
}
