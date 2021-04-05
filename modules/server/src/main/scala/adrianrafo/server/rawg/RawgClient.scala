package adrianrafo.server.rawg

import adrianrafo.server.config.RawgConfig
import adrianrafo.server.persistence.{GameDB, GamesPersistence}
import adrianrafo.server.rawg.game.Game
import cats.effect.Sync
import cats.implicits._
import io.chrisdavenport.log4cats.Logger
import fs2._
import org.http4s.Uri
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.client.Client

class RawgClient[F[_] : Sync](config: RawgConfig, client: Client[F], L: Logger[F], persistence: GamesPersistence[F]) {

  val defaultGameUri: Uri = (config.baseUri / "games")
    .withQueryParam("page_size", Integer.MAX_VALUE)
  //.withQueryParam("key", config.token)

  def getGames(uri: Uri = defaultGameUri): Stream[F, Int] =
    Stream
      .eval(L.info(s"Calling $uri") *> client.expect[PagedResponse[Game]](uri))
      .flatMap { response =>
        val responseStream =
          if (response.results.nonEmpty)
            Stream.eval[F, Int](persistence.insert(response.results.flatMap(GameDB(_)))).attempt.evalMap {
              case Right(n) => Sync[F].pure(n)
              case Left(ex) => L.error(ex)(s"Failure inserting games: ${ex.getMessage}").as(0)
            }
          else Stream.emit(0)
        response.next.fold(responseStream)(responseStream ++ getGames(_))
      }
      .reduce(_ + _)

}
