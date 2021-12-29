package adrianrafo.server.rawg

import adrianrafo.server.config.RawgConfig
import adrianrafo.server.rawg.game.Game
import cats.Id
import cats.effect.Async
import cats.implicits.{catsSyntaxApplicativeId, none}
import fs2._
import org.http4s.Uri
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.client.Client
import org.typelevel.log4cats.Logger

class RawgClient[F[_] : Async](config: RawgConfig, client: Client[F], L: Logger[F]) {

  val defaultGameUri: Uri = config.baseUri / "games"

  def getGames(
    startPage: Option[Int],
    pageSize: Option[Int],
    baseUri: Uri = defaultGameUri
  ): Stream[F, List[Game[Id]]] = {
    def call(uri: Uri): Stream[F, List[Game[Id]]] =
      for {
        _ <- Stream.eval(L.info(s"Calling ${uri.withOptionQueryParam("key", none[String])}"))
        response <- Stream.eval(client.expect[PagedResponse[Game[Option]]](uri))
        responseStream = Stream.emit(response.results.foldLeft(List.empty[Game[Id]]) { case (acc, game) =>
          game.released.fold(acc)(released => acc :+ game.toG(released.pure[Id]))
        })
        _ <- responseStream.evalMap(afterFilter =>
          L.debug(s"Got ${response.results.size}, After filter ${afterFilter.size}")
        )
        result <- response.next.fold[Stream[F, List[Game[Id]]]](responseStream)(responseStream ++ call(_))
      } yield result

    val uri = baseUri
      .withQueryParam("key", config.token)
      .withOptionQueryParam("page", startPage)
      .withQueryParam("page_size", pageSize.getOrElse(config.defaultPageSize))
      .withQueryParam("ordering", "-released")

    call(uri)
  }

}
