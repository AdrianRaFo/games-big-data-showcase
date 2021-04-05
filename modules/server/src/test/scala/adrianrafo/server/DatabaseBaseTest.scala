package adrianrafo.server

import adrianrafo.server.config.Migrations
import cats.effect.{ContextShift, IO}
import com.dimafeng.testcontainers.{ForAllTestContainer, PostgreSQLContainer}
import doobie.Transactor
import org.scalatest.funsuite.AnyFunSuite

import scala.concurrent.ExecutionContext

trait DatabaseBaseTest extends AnyFunSuite with ForAllTestContainer {

  implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  private val driverName: String = "org.postgresql.Driver"

  override val container = PostgreSQLContainer.Def().createContainer()

  //noinspection TypeAnnotation
  // IMPORTANT: MUST BE LAZY VAL
  lazy val transactor = Transactor.fromDriverManager[IO](
    driverName,
    container.jdbcUrl,
    container.username,
    container.password
  )

  override def afterStart(): Unit = {
    super.afterStart()
    Migrations.makeMigrations[IO](container.jdbcUrl, container.username, container.password).unsafeRunSync
  }

}
