package adrianrafo.server

import org.scalatest.matchers.should.Matchers
import doobie.implicits._

class DatabaseTest extends DatabaseBaseTest with Matchers {

  test("Test doobie connection") {
    sql"SELECT name FROM games where id='123e4567-e89b-12d3-a456-426614174000'"
      .query[String]
      .unique
      .transact(transactor).unsafeRunSync() shouldBe "Cyberpunk 2077"
  }

}
