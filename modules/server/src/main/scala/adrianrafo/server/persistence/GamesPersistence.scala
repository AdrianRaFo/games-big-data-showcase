package adrianrafo.server.persistence

import cats.effect.Sync
import cats.implicits._
import doobie.Transactor
import doobie.implicits._

class GamesPersistence[F[_] : Sync](tx: Transactor[F]) {

  def getAll: F[List[GameDB]] = GamesQueries.getAll.to[List].transact(tx)

  def insert(games: List[GameDB]): F[Int] = GamesQueries.insert.updateMany(games).transact(tx)

  def countAll: F[Int] = GamesQueries.countAll.unique.transact(tx)

}
