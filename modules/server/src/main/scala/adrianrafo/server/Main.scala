package adrianrafo.server

import cats.effect._

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = Server.serve[IO].compile.drain.as(ExitCode.Success)
}
