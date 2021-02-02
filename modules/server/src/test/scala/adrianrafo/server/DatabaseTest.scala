package adrianrafo.server

import adrianrafo.server.persistence.GameQueries
import org.scalatest.matchers.should.Matchers
import doobie.scalatest.IOChecker

class DatabaseTest extends DatabaseBaseTest with IOChecker with Matchers {

  test("GameQueries#insert should work as expected") {
    check(GameQueries.insert)
  }

  test("GameQueries#getAll should work as expected") {
    check(GameQueries.getAll)
  }

}
