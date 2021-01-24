package adrianrafo.server.persistence

import adrianrafo.server.rawg.game.Game
import doobie._
import doobie.implicits._

object GameQueries {

  val insert: Update[Game] = Update[Game](
    """INSERT INTO games (
       id,
       slug,
       name,
       released,
       tba,
       rating,
       ratingTop,
       ratings,
       ratingsCount,
       metacritic,
       playtime,
       updated,
       reviewsCount,
       platform,
       parentPlatforms,
       genres,
       stores,
       tags,
       esrb_rating
       ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"""
  )

  val getAll =
    sql"""SELECT
         id,
         slug,
         name,
         released,
         tba,
         rating,
         ratingTop,
         ratings,
         ratingsCount,
         metacritic,
         playtime,
         updated,
         reviewsCount,
         platform,
         parentPlatforms,
         genres,
         stores,
         tags,
         esrb_rating
       FROM games
       """

}
