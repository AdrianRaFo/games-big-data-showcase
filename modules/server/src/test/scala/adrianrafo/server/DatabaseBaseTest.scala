package adrianrafo.server

import adrianrafo.server.config.Migrations
import cats.effect.{ContextShift, IO}
import com.dimafeng.testcontainers.PostgreSQLContainer
import com.dimafeng.testcontainers.munit.TestContainerForAll
import doobie.Transactor
import munit.FunSuite

import scala.concurrent.ExecutionContext

trait DatabaseBaseTest extends FunSuite with TestContainerForAll {

  implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  private val driverName: String = "org.postgresql.Driver"

  override val containerDef: PostgreSQLContainer.Def = PostgreSQLContainer.Def()

  //noinspection TypeAnnotation
  // IMPORTANT: MUST BE LAZY VAL
  lazy val transactor = withContainers { container =>
    Transactor.fromDriverManager[IO](
      driverName,
      container.jdbcUrl,
      container.username,
      container.password
    )
  }

  override def beforeAll(): Unit = {
    super.beforeAll()
    withContainers { container =>
      Migrations.makeMigrations[IO](container.jdbcUrl, container.username, container.password).unsafeRunSync
    }
  }

}
