package adrianrafo.server.rawg

import enumeratum.EnumEntry.Hyphencase
import enumeratum._
import enumeratum.values.StringEnumEntry
import org.http4s.Uri

import java.time.{LocalDate, LocalDateTime, ZonedDateTime}

final case class PagedResponse[A](count: Int, next: Option[Uri], previous: Option[Uri], results: List[A])

final case class Developer(id: Int, name: String, slug: String, games_count: Int, imageBackground: Uri)

final case class GameRatings(id: Int, title: String, count: Int, percent: Double)

final case class GameAddedByStatus(yet: Int, owned: Int, beaten: Int, toplay: Int, dropped: Int, playing: Int)

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

final case class EsrbRating(id: Int, slug: EsrbSlug, name: EsrbName)

final case class GamePlatform(
  platform: GamePlatform.Platform,
  releasedAt: Option[String],
  requirements: Option[GamePlatform.Requirements]
)

object GamePlatform {

  final case class Platform(id: Integer, slug: String, name: String)

  final case class Requirements(minimum: String, recommended: String)

}

final case class Game(
  id: Int,
  slug: String,
  name: String,
  released: LocalDate,
  tba: Boolean,
  backgroundImage: Uri,
  rating: Int,
  ratingTop: Int,
  ratings: List[GameRatings],
  ratingsCount: Int,
  reviewsTextCount: String,
  added: Int,
  addedByStatus: GameAddedByStatus,
  metacritic: Int,
  playtime: Int,
  suggestionsCount: Int,
  updated: LocalDateTime,
  reviewsCount: Int,
  esrbRating: Option[EsrbRating],
  platform: List[GamePlatform]
)
