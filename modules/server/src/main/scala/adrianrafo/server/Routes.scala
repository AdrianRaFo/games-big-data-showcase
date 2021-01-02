package adrianrafo.server

import adrianrafo.server.config.RawgConfig
import cats.effect.Sync
import cats.implicits._
import io.circe.Json
import org.http4s._
import org.http4s.client.Client
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.CirceEntityDecoder._

class Routes[F[_] : Sync](config: RawgConfig, client: Client[F]) extends Http4sDsl[F] {

  val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case _@POST -> Root / "transfer" / "data" =>
      client.expect[Json](s"${config.baseUri}") *> Ok()
  }

}

object Routes {
  def apply[F[_] : Sync](config: RawgConfig, client: Client[F]): HttpRoutes[F] =
    new Routes(config, client).httpRoutes
}
