package adrianrafo.server.rawg

import adrianrafo.server.config.RawgConfig
import adrianrafo.server.rawg.game.Game
import cats.effect.Sync
import cats.implicits._
import io.chrisdavenport.log4cats.Logger
import fs2._
import org.http4s.Uri
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.client.Client

class RawgClient[F[_] : Sync](client: Client[F], config: RawgConfig)(L: Logger[F]) {

  val defaultGameUri: Uri = (config.baseUri / "games")
    .withQueryParam("page_size", Integer.MAX_VALUE)
  //.withQueryParam("key", config.token)

  def getGames(uri: Uri = defaultGameUri): Stream[F, PagedResponse[Game]] =
    Stream
      .eval(L.info(s"Calling $uri") *> client.expect[PagedResponse[Game]](uri))
      .flatMap { response =>
        val responseStream = Stream.emit[F, PagedResponse[Game]](response)
        response.next.fold(responseStream)(responseStream ++ getGames(_))
      }

}
