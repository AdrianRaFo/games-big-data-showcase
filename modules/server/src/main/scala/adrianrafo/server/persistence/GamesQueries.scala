package adrianrafo.server.persistence

import doobie._
import doobie.implicits._
import doobie.postgres.implicits._

object GamesQueries {

  val insert: Update[GameDB] = Update[GameDB](
    """INSERT INTO games (
       id,
       slug,
       name,
       released,
       tba,
       rating,
       rating_top,
       ratings_count,
       metacritic,
       playtime,
       updated,
       reviews_count,
       platform_slug,
       platform_released_at,
       parent_platforms,
       genres,
       stores,
       tags,
       esrb_rating,
       exceptional_rated_count,
       exceptional_rated_percent,
       recommended_rated_count,
       recommended_rated_percent,
       meh_rated_count,
       meh_rated_percent,
       skip_rated_count,
       skip_rated_percent
       ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"""
  )

  val getAll: Query0[GameDB] =
    sql"""SELECT
         id,
         slug,
         name,
         released,
         tba,
         rating,
         rating_top,
         ratings_count,
         metacritic,
         playtime,
         updated,
         reviews_count,
         platform_slug,
         platform_released_at,
         parent_platforms,
         genres,
         stores,
         tags,
         esrb_rating,
         exceptional_rated_count,
         exceptional_rated_percent,
         recommended_rated_count,
         recommended_rated_percent,
         meh_rated_count,
         meh_rated_percent,
         skip_rated_count,
         skip_rated_percent
       FROM games
       """.query[GameDB]

  val countAll = sql"select count(*) from games".query[Int]

}
