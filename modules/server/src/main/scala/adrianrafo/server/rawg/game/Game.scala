package adrianrafo.server.rawg.game

import io.circe._

import java.time.{LocalDate, LocalDateTime}

final case class Game(
  id: Int,
  slug: String,
  name: String,
  released: LocalDate,
  tba: Boolean, //To be announced
  rating: Double,
  ratingTop: Int,
  ratings: List[Ratings],
  ratingsCount: Int,
  metacritic: Option[Int],
  playtime: Int,
  updated: LocalDateTime,
  reviewsCount: Int,
  platforms: List[Platform],
  parentPlatforms: List[ParentPlatform],
  genres: List[Genre],
  stores: List[Store],
  tags: List[Tag],
  esrbRating: Option[EsrbRating]
)

object Game {
  implicit val gameDecoder: Decoder[Game] = Decoder.forProduct19(
    "id",
    "slug",
    "name",
    "released",
    "tba",
    "rating",
    "rating_top",
    "ratings",
    "ratings_count",
    "metacritic",
    "playtime",
    "updated",
    "reviews_count",
    "platforms",
    "parent_platforms",
    "genres",
    "stores",
    "tags",
    "esrb_rating"
  )(Game.apply)
}
