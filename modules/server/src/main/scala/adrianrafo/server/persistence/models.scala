package adrianrafo.server.persistence

import adrianrafo.server.rawg.game.{EsrbRating, Game, RatingSlug}

import java.time.{LocalDate, LocalDateTime}

final case class GameDB(
  id: Int,
  slug: String,
  name: String,
  released: LocalDate,
  tba: Boolean, //To be announced
  rating: Int,
  ratingTop: Int,
  ratingsCount: Int,
  metacritic: Int,
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

object GameDB {
  def apply(game: Game): List[GameDB] = {

    def getRatingCount(slug: RatingSlug): Int = game.ratings.find(_.title == slug).map(_.count).getOrElse(0)

    def getRatingPercent(slug: RatingSlug): Double = game.ratings.find(_.title == slug).map(_.percent).getOrElse(0d)

    game.platform.map(platform =>
      new GameDB(
        id = game.id,
        slug = game.slug,
        name = game.name,
        released = game.released,
        tba = game.tba,
        rating = game.rating,
        ratingTop = game.ratingTop,
        ratingsCount = game.ratingsCount,
        metacritic = game.metacritic,
        playtime = game.playtime,
        updated = game.updated,
        reviewsCount = game.reviewsCount,
        platformSlug = platform.slug,
        platformReleasedAt = platform.releasedAt,
        parentPlatforms = game.parentPlatforms.map(_.slug),
        genres = game.genres.map(_.slug),
        stores = game.stores.map(_.slug),
        tags = game.tags.map(_.slug),
        esrbRating = game.esrbRating,
        exceptionalRatedCount = getRatingCount(RatingSlug.Exceptional),
        exceptionalRatedPercent = getRatingPercent(RatingSlug.Exceptional),
        recommendedRatedCount = getRatingCount(RatingSlug.Recommended),
        recommendedRatedPercent = getRatingPercent(RatingSlug.Recommended),
        mehRatedCount = getRatingCount(RatingSlug.Meh),
        mehRatedPercent = getRatingPercent(RatingSlug.Meh),
        skipRatedCount = getRatingCount(RatingSlug.Skip),
        skipRatedPercent = getRatingPercent(RatingSlug.Skip)
      )
    )
  }
}
