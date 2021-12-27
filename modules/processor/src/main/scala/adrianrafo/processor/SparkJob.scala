package adrianrafo.processor

import org.apache.spark.sql.SparkSession

object SparkJob {

  def main(args: Array[String]) = {
    val spark = SparkSession
      .builder()
      .appName("Games big data showcase")
      .config("spark.master", "local")
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    import spark.implicits._
    val df = spark.read
      .option("multiline", "true")
      .json("./modules/processor/src/main/resources/sample.json")
    // json
    val tags = df.select($"tags").as[List[Tag]].flatMap(identity)
    tags.printSchema()
    tags.show(false)
    // sql
    df.createOrReplaceTempView("games")
    val sqlDF = spark.sql("SELECT tags FROM games").as[List[Tag]].flatMap(identity)
    sqlDF.show()
    spark.stop()
  }

}
