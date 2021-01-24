package adrianrafo.server.rawg.game

import io.circe._
import io.circe.generic.semiauto.deriveDecoder

import java.time.{LocalDate, LocalDateTime}

final case class Game(
  id: Int,
  slug: String,
  name: String,
  released: LocalDate,
  tba: Boolean, //To be announced
  rating: Int,
  ratingTop: Int,
  ratings: List[Ratings],
  ratingsCount: Int,
  metacritic: Int,
  playtime: Int,
  updated: LocalDateTime,
  reviewsCount: Int,
  platform: List[Platform],
  parentPlatforms: List[ParentPlatform],
  genres: List[Genre],
  stores: List[Store],
  tags: List[Tag],
  esrbRating: Option[EsrbRating]
)

object Game {
  implicit val gameDecoder: Decoder[Game] = deriveDecoder
}
