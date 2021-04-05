package adrianrafo.server.config

import cats.effect.Sync
import cats.implicits._
import org.flywaydb.core.Flyway

object Migrations {
  def makeMigrations[F[_] : Sync](url: String, user: String, password: String): F[Unit] =
    Sync[F].delay(Flyway.configure.dataSource(url, user, password).load.migrate).void

  def makeMigrationsFromConfig[F[_] : Sync](config: PostgresConfig): F[Unit] =
    makeMigrations(config.jdbcUrl, config.user, config.password)
}
