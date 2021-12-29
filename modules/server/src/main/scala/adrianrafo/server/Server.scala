package adrianrafo.server

import adrianrafo.server.config._
import adrianrafo.server.persistence.GamesPersistence
import adrianrafo.server.rawg.RawgClient
import cats.effect.{Async, ExitCode}
import fs2.Stream
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.client.{Client, JavaNetClientBuilder}
import org.http4s.implicits._
import org.typelevel.log4cats.slf4j.Slf4jLogger
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import pureconfig.module.http4s._

import java.util.concurrent.{ExecutorService, Executors}

object Server {

  val blockingPool: ExecutorService = Executors.newFixedThreadPool(5)

  def serve[F[_] : Async]: fs2.Stream[F, ExitCode] = {
    val logger = Slf4jLogger.getLogger[F]
    val config: Config = ConfigSource.default.loadOrThrow[Config]

    val httpClient: Client[F] = JavaNetClientBuilder[F]
      .withReadTimeout(config.httpClient.requestTimeout)
      .create

    for {
      _ <- Stream.eval(Migrations.makeMigrationsFromConfig(config.postgres))
      transactor <- Stream.eval(ConfigLoader.loadTransactor(config.postgres))
      gamesPersistence = new GamesPersistence[F](transactor)
      rawgClient = new RawgClient[F](config.rawg, httpClient, logger)
      server <- BlazeServerBuilder[F]
        .withHttpApp(Routes[F](rawgClient, logger, gamesPersistence).orNotFound)
        .bindLocal(38437)
        .serve
    } yield server

  }

}
