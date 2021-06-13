package adrianrafo.server

import adrianrafo.server.persistence.GamesQueries
import doobie.munit.IOChecker

class DatabaseTest extends DatabaseBaseTest with IOChecker {

  test("GameQueries#insert should work as expected") {
    check(GamesQueries.insert)
  }

  test("GameQueries#getAll should work as expected") {
    check(GamesQueries.getAll)
  }

}
