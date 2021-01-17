package adrianrafo.server

import adrianrafo.server.rawg.RawgClient
import cats.effect.{Concurrent, Sync}
import cats.implicits._
import io.chrisdavenport.log4cats.Logger
import org.http4s._
import org.http4s.dsl.Http4sDsl

class Routes[F[_] : Sync : Concurrent](rawgClient: RawgClient[F], logger: Logger[F]) extends Http4sDsl[F] {

  val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case _@POST -> Root / "transfer" / "data" / "games" =>
      Concurrent[F].start(rawgClient.getGames().compile.drain.attempt.flatMap {
        case Left(ex) => logger.error(ex)("Unexpected error running games transfer")
        case _        => logger.info("Game transfer successfully finished")
      }) *> Accepted()
  }

}
object Routes {
  def apply[F[_] : Sync : Concurrent](rawgClient: RawgClient[F], logger: Logger[F]): HttpRoutes[F] =
    new Routes(rawgClient, logger).httpRoutes
}
