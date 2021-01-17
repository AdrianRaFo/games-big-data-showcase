package adrianrafo.server.rawg

import cats.implicits._
import io.circe._
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.Uri
import org.http4s.circe._

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

  case class ParentPlatform(id: Int, name: String, slug: String)

  object ParentPlatform {
    implicit val platformDecoder: Decoder[ParentPlatform] = Decoder.instance { h =>
      val platform = h.downField("platform")
      (
        platform.get[Int]("id"),
        platform.get[String]("name"),
        platform.get[String]("slug")
        ).mapN(ParentPlatform.apply)
    }
  }

  case class Genre(id: Int, name: String, slug: String, gamesCount: Int, imageBackground: String)

  case class Store(id: Int, name: String, slug: String, domain: String, gamesCount: Int, imageBackground: Uri)

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

  case class Clip(clip320p: Uri, clip640p: Uri, full: Uri, video: String, preview: Uri)

  object Clip {

    implicit val clipDecoder: Decoder[Clip] = Decoder.instance { h =>
      val clips = h.downField("clips")
      (
        clips.get[Uri]("320"),
        clips.get[Uri]("640"),
        clips.get[Uri]("full"),
        h.get[String]("video"),
        h.get[Uri]("preview")
        ).mapN(Clip.apply)
    }

  }

  case class Tag(id: Int, name: String, slug: String, language: String, gamesCount: Int, imageBackground: String)

  case class ShortScreenshot(id: Int, image: String)

}
