package adrianrafo.processor.models

import java.time.{LocalDate, LocalDateTime}

final case class GameDB(
  id: Int,
  slug: String,
  name: String,
  released: LocalDate,
  tba: Boolean, // To be announced
  rating: Double,
  ratingTop: Int,
  ratingsCount: Int,
  metacritic: Option[Int],
  playtime: Int,
  updated: LocalDateTime,
  reviewsCount: Int,
  platformSlug: String,
  platformReleasedAt: Option[String],
  parentPlatforms: List[String],
  genres: List[String],
  stores: List[String],
  tags: List[String],
  esrbRating: Option[EsrbRating],
  exceptionalRatedCount: Int,
  exceptionalRatedPercent: Double,
  recommendedRatedCount: Int,
  recommendedRatedPercent: Double,
  mehRatedCount: Int,
  mehRatedPercent: Double,
  skipRatedCount: Int,
  skipRatedPercent: Double
)
