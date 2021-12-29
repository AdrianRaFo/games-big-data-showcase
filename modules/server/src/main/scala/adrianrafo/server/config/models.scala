package adrianrafo.server.config

import org.http4s.Uri

import scala.concurrent.duration.FiniteDuration

final case class RawgConfig(baseUri: Uri, token: String, defaultPageSize: Int)

final case class PostgresConfig(
  driver: String,
  jdbcUrl: String,
  user: String,
  password: String
)

final case class HttpClientConfig(
  requestTimeout: FiniteDuration
)

final case class Config(rawg: RawgConfig, postgres: PostgresConfig, httpClient: HttpClientConfig)
