package adrianrafo.server

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = Server.serve.compile.drain.as(ExitCode.Success)
}
