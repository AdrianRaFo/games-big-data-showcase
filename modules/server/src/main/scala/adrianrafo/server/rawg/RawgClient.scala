package adrianrafo.server.rawg

import adrianrafo.server.config.RawgConfig
import cats.effect.Sync
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.client.Client

class RawgClient[F[_] : Sync](client: Client[F], config: RawgConfig) {

  def getGames: F[PagedResponse[Game]] = {
    val uri = config.baseUri / "games"
    client.expect[PagedResponse[Game]](uri)
  }

}
