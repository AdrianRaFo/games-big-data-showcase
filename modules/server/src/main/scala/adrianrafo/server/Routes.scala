package adrianrafo.server

import adrianrafo.server.Routes._
import adrianrafo.server.persistence.{GameDB, GamesPersistence}
import adrianrafo.server.rawg.RawgClient
import adrianrafo.server.rawg.game.Game
import cats.Id
import cats.effect.Async
import cats.implicits._
import fs2.Stream
import org.http4s._
import org.http4s.circe.CirceEntityEncoder.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.OptionalQueryParamDecoderMatcher
import org.typelevel.log4cats.Logger

class Routes[F[_] : Async](rawgClient: RawgClient[F], logger: Logger[F], persistence: GamesPersistence[F])
  extends Http4sDsl[F] {

  val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case POST -> Root / "transfer" / "data" / "games" :? StartPageParamMatcher(startPage) +& PageSizeParamMatcher(
    pageSize
    ) =>
      def insert(result: List[Game[Id]]) =
        Stream.eval[F, Int](persistence.insert(result.flatMap(GameDB(_)))).attempt.evalMap {
          case Right(n) => logger.debug(s"Retrieved games inserted as $n games").as(n)
          case Left(ex) => logger.error(ex)(s"Failure inserting games: ${ex.getMessage}").as(0)
        }

      Async[F].start(
        rawgClient
          .getGames(startPage, pageSize)
          .flatMap(insert)
          .reduce(_ + _)
          .compile
          .lastOrError
          .attempt
          .flatMap {
            case Right(n) => logger.info(s"Game transfer successfully finished after insert $n game")
            case Left(ex) => logger.error(ex)("Unexpected error running games transfer")
          }
      ) *> Accepted()

    case GET -> Root / "data" / "games" / "count" => persistence.countAll.flatMap(Ok(_))
  }

}
object Routes {

  def apply[F[_] : Async](
    rawgClient: RawgClient[F],
    logger: Logger[F],
    persistence: GamesPersistence[F]
  ): HttpRoutes[F] =
    new Routes(rawgClient, logger, persistence).httpRoutes

  object StartPageParamMatcher extends OptionalQueryParamDecoderMatcher[Int]("start_page")

  object PageSizeParamMatcher extends OptionalQueryParamDecoderMatcher[Int]("page_size")

}
