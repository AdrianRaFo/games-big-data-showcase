package adrianrafo.server

import adrianrafo.server.config.Migrations
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.dimafeng.testcontainers.PostgreSQLContainer
import com.dimafeng.testcontainers.munit.TestContainerForAll
import doobie.Transactor
import munit.FunSuite

trait DatabaseBaseTest extends FunSuite with TestContainerForAll {

  private val driverName: String = "org.postgresql.Driver"

  override val containerDef: PostgreSQLContainer.Def = PostgreSQLContainer.Def()

  // noinspection TypeAnnotation
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
      Migrations.makeMigrations[IO](container.jdbcUrl, container.username, container.password).unsafeRunSync()
    }
  }

}
