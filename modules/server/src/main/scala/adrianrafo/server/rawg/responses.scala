package adrianrafo.server.rawg

import cats.implicits._
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.Uri

import java.time.{LocalDate, LocalDateTime}

final case class PagedResponse[A](count: Int, next: Option[Uri], previous: Option[Uri], results: List[A])

final case class Developer(id: Int, name: String, slug: String, games_count: Int, imageBackground: Uri)

final case class Game(
  id: Int,
  slug: String,
  name: String,
  released: LocalDate,
  tba: Boolean,
  backgroundImage: Uri,
  rating: Int,
  ratingTop: Int,
  ratings: List[Game.Ratings],
  ratingsCount: Int,
  reviewsTextCount: String,
  added: Int,
  addedByStatus: Game.AddedByStatus,
  metacritic: Int,
  playtime: Int,
  suggestionsCount: Int,
  updated: LocalDateTime,
  reviewsCount: Int,
  platform: List[Game.Platform],
  parentPlatforms: List[Game.ParentPlatform],
  genres: List[Game.Genre],
  stores: List[Game.Store],
  clip: Game.Clip,
  tags: List[Game.Tag],
  esrbRating: Option[Game.EsrbRating],
  shortScreenshots: List[Game.ShortScreenshot]
)

object Game {

  final case class Ratings(id: Int, title: String, count: Int, percent: Double)

  final case class AddedByStatus(yet: Int, owned: Int, beaten: Int, toplay: Int, dropped: Int, playing: Int)

  final case class EsrbRating(id: Int, slug: EsrbSlug, name: EsrbName)

  final case class Platform(
    id: Integer,
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
        platform.get[Integer]("id"),
        platform.get[String]("name"),
        platform.get[String]("slug"),
        h.get[Option[String]]("releasedAt"),
        h.get[Option[Platform.Requirements]]("requirements_en")
        ).mapN(Platform.apply)
    }

  }

  case class ParentPlatform() //TODO

  case class Genre() //TODO

  case class Store() //TODO

  case class Clip() //TODO

  case class Tag() //TODO

  case class ShortScreenshot() //TODO

}
