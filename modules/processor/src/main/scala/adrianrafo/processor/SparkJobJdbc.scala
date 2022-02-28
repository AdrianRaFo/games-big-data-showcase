package adrianrafo.processor

import adrianrafo.processor.config.{Config, PostgresConfig}
import org.apache.spark.sql.SparkSession
import pureconfig.ConfigSource
import pureconfig.generic.auto._

object SparkJobJdbc {

  def main(args: Array[String]) = {
    val config: PostgresConfig = ConfigSource.default.loadOrThrow[Config].postgres

    val spark = SparkSession
      .builder()
      .appName("Games big data showcase")
      .config("spark.master", "local")
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    import spark.implicits._

    def jdbcReader = spark.read
      .format("jdbc")
      .option("url", config.jdbcUrl)
      .option("user", config.user)
      .option("password", config.password)
      .option("driver", config.driver)

    // using whole table
    val tableDF = jdbcReader
      .option("dbtable", "games")
      .load()

    val topRatedGenre = tableDF
      .select("genres", "metacritic")
      .where($"metacritic".isNotNull)
      .as[(List[String], Int)]
      .flatMap { case (gameGenres, gameRating) =>
        gameGenres.map(it => (it, gameRating))
      }
      .withColumnRenamed("_1", "genre")
      .withColumnRenamed("_2", "rating")
      .groupBy("genre")
      .avg("rating")
      .sort($"avg(rating)".desc)

    topRatedGenre.show(false)

    // using query
    //    val queryDF = jdbcReader
    //      .option("query", "select genres, metacritic from games where metacritic is not null")
    //      .load()
    //
    //    val genreRating = queryDF
    //      .select("genres", "metacritic")
    //      .as[(List[String], Int)]
    //      .flatMap { case (gameGenres, gameRating) =>
    //        gameGenres.map(it => (it, gameRating))
    //      }
    //      .withColumnRenamed("_1", "genre")
    //      .withColumnRenamed("_2", "rating")
    //
    //    genreRating.createOrReplaceTempView("rated_genres")
    //
    //    val sqlDF =
    //      spark.sql("SELECT genre, avg(rating) FROM rated_genres group by genre").sort($"avg(rating)".desc)
    //
    //    sqlDF.show(false)

    spark.stop()
  }

}
