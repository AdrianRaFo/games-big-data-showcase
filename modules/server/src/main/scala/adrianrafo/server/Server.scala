package adrianrafo.server

import adrianrafo.server.config.{Config, ConfigLoader, Migrations}
import adrianrafo.server.persistence.GamesPersistence
import adrianrafo.server.rawg.RawgClient
import cats.effect.{Blocker, ConcurrentEffect, ContextShift, ExitCode, Timer}
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import org.http4s.client.{Client, JavaNetClientBuilder}
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import pureconfig.module.http4s._

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.ExecutionContext
import fs2.Stream

object Server {

  val blockingPool: ExecutorService = Executors.newFixedThreadPool(5)

  def serve[F[_] : ContextShift : ConcurrentEffect : Timer]: fs2.Stream[F, ExitCode] = {
    val logger = Slf4jLogger.getLogger[F]
    val config: Config = ConfigSource.default.loadOrThrow[Config]

    val httpClient: Client[F] = JavaNetClientBuilder[F](Blocker.liftExecutorService(blockingPool))
      .withReadTimeout(config.httpClient.requestTimeout)
      .create

    for {
      _ <- Stream.eval(Migrations.makeMigrationsFromConfig(config.postgres))
      transactor <- Stream.eval(ConfigLoader.loadTransactor(config.postgres))
      gamesPersistence = new GamesPersistence[F](transactor)
      rawgClient = new RawgClient[F](config.rawg, httpClient, logger, gamesPersistence)
      server <-
        BlazeServerBuilder
          .apply(ExecutionContext.global)
          .withHttpApp(Routes[F](rawgClient, logger).orNotFound)
          .bindLocal(38437)
          .serve
    } yield server

  }

}
