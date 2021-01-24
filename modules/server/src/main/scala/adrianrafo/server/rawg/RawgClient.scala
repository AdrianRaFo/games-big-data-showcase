package adrianrafo.server.rawg

import adrianrafo.server.config.RawgConfig
import adrianrafo.server.rawg.game.Game
import cats.effect.Sync
import org.http4s.Uri
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.client.Client
import fs2._

class RawgClient[F[_] : Sync](client: Client[F], config: RawgConfig) {

  val defaultGameUri: Uri = (config.baseUri / "games").withQueryParam("page_size", Integer.MAX_VALUE)

  def getGames(uri: Uri = defaultGameUri): Stream[F, PagedResponse[Game]] =
    Stream
      .eval(client.expect[PagedResponse[Game]](uri))
      .flatMap { response =>
        val responseStream = Stream.emit[F, PagedResponse[Game]](response)
        response.next.fold(responseStream)(responseStream ++ getGames(_))
      }

}
