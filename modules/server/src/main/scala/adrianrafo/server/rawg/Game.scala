package adrianrafo.server.rawg

import org.http4s.Uri

import java.time.{LocalDate, LocalDateTime}
import io.circe._
import cats.implicits._
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.circe._

final case class Game(
  id: Int,
  slug: String,
  name: String,
  released: LocalDate,
  tba: Boolean, //To be announced
  rating: Int,
  ratingTop: Int,
  ratings: List[Game.Ratings],
  ratingsCount: Int,
  metacritic: Int,
  playtime: Int,
  updated: LocalDateTime,
  reviewsCount: Int,
  platform: List[Game.Platform],
  parentPlatforms: List[Game.ParentPlatform],
  genres: List[Game.Genre],
  stores: List[Game.Store],
  tags: List[Game.Tag],
  esrbRating: Option[Game.EsrbRating]
)

object Game {

  final case class Ratings(id: Int, title: RatingSlug, count: Int, percent: Double)

  object Ratings {
    implicit val ratingsDecoder: Decoder[Ratings] = deriveDecoder
  }

  final case class EsrbRating(id: Int, slug: EsrbSlug, name: String)

  object EsrbRating {
    implicit val esrbRatingDecoder: Decoder[EsrbRating] = deriveDecoder
  }

  final case class Platform(
    id: Int,
    name: String,
    slug: String,
    releasedAt: Option[String],
    requirements: Option[Platform.Requirements]
  )

  object Platform {

    final case class Requirements(minimum: String, recommended: String)

    object Requirements {
      implicit val requirementsDecoder: Decoder[Requirements] = deriveDecoder
    }

    implicit val platformDecoder: Decoder[Platform] = Decoder.instance { h =>
      val platform = h.downField("platform")
      (
        platform.get[Int]("id"),
        platform.get[String]("name"),
        platform.get[String]("slug"),
        h.get[Option[String]]("releasedAt"),
        h.get[Option[Platform.Requirements]]("requirements_en")
        ).mapN(Platform.apply)
    }

  }

  final case class ParentPlatform(id: Int, name: String, slug: String)

  object ParentPlatform {
    implicit val parentPlatformDecoder: Decoder[ParentPlatform] = Decoder.instance { h =>
      val platform = h.downField("platform")
      (
        platform.get[Int]("id"),
        platform.get[String]("name"),
        platform.get[String]("slug")
        ).mapN(ParentPlatform.apply)
    }
  }

  final case class Genre(id: Int, name: String, slug: String, gamesCount: Int, imageBackground: String)

  object Genre {
    implicit val genreDecoder: Decoder[Genre] = deriveDecoder
  }

  final case class Store(id: Int, name: String, slug: String, domain: String, gamesCount: Int, imageBackground: Uri)

  object Store {
    implicit val storeDecoder: Decoder[Store] = Decoder.instance { h =>
      val store = h.downField("store")
      (
        store.get[Int]("id"),
        store.get[String]("name"),
        store.get[String]("slug"),
        store.get[String]("domain"),
        store.get[Int]("games_count"),
        store.get[Uri]("image_background")
        ).mapN(Store.apply)
    }
  }

  final case class Tag(id: Int, name: String, slug: String, language: String, gamesCount: Int, imageBackground: String)

  object Tag {
    implicit val tagDecoder: Decoder[Tag] = deriveDecoder
  }

  implicit val gameDecoder: Decoder[Game] = deriveDecoder

}
