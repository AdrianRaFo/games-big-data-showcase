package adrianrafo.server.rawg.game

import doobie.Meta
import doobie.postgres.implicits._
import enumeratum.EnumEntry._
import enumeratum._
import io.circe.Decoder

sealed trait EsrbRating extends Hyphencase

object EsrbRating extends Enum[EsrbRating] {

  case object Everyone extends EsrbRating

  case object Everyone10Plus extends EsrbRating {
    override def entryName: String = "everyone-10-plus"
  }

  case object Teen extends EsrbRating

  case object Mature extends EsrbRating

  case object AdultsOnly extends EsrbRating

  case object RatingPending extends EsrbRating

  val values = findValues

  implicit val esrbRatingDecoder = Decoder.instance { h =>
    h.downField("slug").as[String].map(EsrbRating.withName)
  }

  implicit lazy val enumMeta: Meta[EsrbRating] = pgEnumString("esrb_rating", EsrbRating.withName, _.entryName)

}

sealed trait RatingSlug extends EnumEntry with Lowercase

object RatingSlug extends Enum[RatingSlug] with CirceEnum[RatingSlug] {

  case object Exceptional extends RatingSlug

  case object Recommended extends RatingSlug

  case object Meh extends RatingSlug

  case object Skip extends RatingSlug

  val values = findValues
}
