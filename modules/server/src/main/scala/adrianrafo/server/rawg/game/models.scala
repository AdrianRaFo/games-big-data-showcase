package adrianrafo.server.rawg.game

import cats.implicits._
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class Ratings(title: RatingSlug, count: Int, percent: Double)

object Ratings {
  implicit val ratingsDecoder: Decoder[Ratings] = deriveDecoder
}

final case class Platform(
  slug: String,
  releasedAt: Option[String],
  requirements: Option[Platform.Requirements]
)

object Platform {

  final case class Requirements(minimum: Option[String], recommended: Option[String])

  object Requirements {
    implicit val requirementsDecoder: Decoder[Requirements] = deriveDecoder
  }

  implicit val platformDecoder: Decoder[Platform] = Decoder.instance { h =>
    (
      h.downField("platform").get[String]("slug"),
      h.get[Option[String]]("released_at"),
      h.get[Option[Platform.Requirements]]("requirements_en")
      ).mapN(Platform.apply)
  }

}

final case class ParentPlatform(slug: String)

object ParentPlatform {
  implicit val parentPlatformDecoder: Decoder[ParentPlatform] =
    Decoder.instance(_.downField("platform").get[String]("slug").map(ParentPlatform(_)))
}

final case class Genre(slug: String)

object Genre {
  implicit val genreDecoder: Decoder[Genre] = Decoder.forProduct1("slug")(Genre.apply)
}

final case class Store(slug: String)

object Store {
  implicit val storeDecoder: Decoder[Store] = Decoder.instance { h =>
    h.downField("store").get[String]("slug").map(Store(_))
  }
}

final case class Tag(slug: String, language: String, gamesCount: Int, imageBackground: Option[String])

object Tag {
  implicit val tagDecoder: Decoder[Tag] = Decoder.forProduct4(
    "slug",
    "language",
    "games_count",
    "image_background"
  )(Tag.apply)
}
