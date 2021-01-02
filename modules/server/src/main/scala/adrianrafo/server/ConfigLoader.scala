package adrianrafo.server

import adrianrafo.server.config.PostgresConfig
import cats.effect.{Async, Blocker, ContextShift, Sync}
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor

object ConfigLoader {
  def loadTransactor[F[_] : Async : ContextShift](dbConfig: PostgresConfig): F[Transactor[F]] =
    Sync[F].delay(
      Transactor.fromDriverManager[F](
        dbConfig.driver,
        dbConfig.jdbcUrl,
        dbConfig.user,
        dbConfig.password,
        Blocker.liftExecutionContext(ExecutionContexts.synchronous)
      )
    )

}
