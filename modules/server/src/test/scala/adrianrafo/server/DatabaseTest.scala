package adrianrafo.server

import adrianrafo.server.persistence.GamesQueries
import org.scalatest.matchers.should.Matchers
import doobie.scalatest.IOChecker

class DatabaseTest extends DatabaseBaseTest with IOChecker with Matchers {

  test("GameQueries#insert should work as expected") {
    check(GamesQueries.insert)
  }

  test("GameQueries#getAll should work as expected") {
    check(GamesQueries.getAll)
  }

}
