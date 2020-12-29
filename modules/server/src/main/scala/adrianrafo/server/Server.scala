package adrianrafo.server

import cats.effect.{Blocker, ConcurrentEffect, ContextShift, ExitCode, Timer}
import org.http4s.implicits._
import org.http4s.client.{Client, JavaNetClientBuilder}
import org.http4s.server.blaze.BlazeServerBuilder
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import pureconfig.module.http4s._

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.ExecutionContext

object Server {

  def serve[F[_] : ContextShift : ConcurrentEffect : Timer]: fs2.Stream[F, ExitCode] = {
    val blockingPool: ExecutorService = Executors.newFixedThreadPool(5)
    val config: RawgConfig = ConfigSource.default.loadOrThrow[RawgConfig]
    val httpClient: Client[F] = JavaNetClientBuilder[F](Blocker.liftExecutorService(blockingPool)).create
    BlazeServerBuilder.apply(ExecutionContext.global).withHttpApp(Routes[F](config, httpClient).orNotFound).serve
  }

}
