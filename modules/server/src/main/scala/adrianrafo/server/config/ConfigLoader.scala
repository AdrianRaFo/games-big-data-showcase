package adrianrafo.server.config

import cats.effect.Async
import doobie.util.transactor.Transactor

object ConfigLoader {

  def loadTransactor[F[_] : Async](dbConfig: PostgresConfig): F[Transactor[F]] =
    Async[F].delay(
      Transactor.fromDriverManager[F](
        dbConfig.driver,
        dbConfig.jdbcUrl,
        dbConfig.user,
        dbConfig.password
      )
    )

}
