package edu.ucr.cs.cs167.madhi002

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.{explode, udf}
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable

object AnalyzeTweets {

  def main(args: Array[String]) {
    val operation: String = args(0)
    val inputfile: String = args(1)

    val conf = new SparkConf
    if (!conf.contains("spark.master"))
      conf.setMaster("local[*]")

    println(s"Using Spark master '${conf.get("spark.master")}'")

    val spark = SparkSession
      .builder()
      .appName("CS167 Lab7 - SQL")
      .config(conf)
      .getOrCreate()

    val getTopLangs : UserDefinedFunction = udf((x: mutable.WrappedArray[String]) => {
      val orderedLangCount = x.groupBy(identity).map { case (x, y) => x -> y.size }.toArray.sortBy(-1 * _._2)
      orderedLangCount.take(5.min(orderedLangCount.length))
    })
    spark.udf.register("getTopLangs", getTopLangs)

    if (!(inputfile.endsWith(".json") || inputfile.endsWith(".parquet"))) {
      Console.err.println(s"Unexpected input format. Expected file name to end with either '.json' or '.parquet'.")
    }
    var df : DataFrame = if (inputfile.endsWith(".json")) {
      spark.read.json(inputfile)
    } else {
      spark.read.parquet(inputfile)
    }
    df.createOrReplaceTempView("tweets")

    try {
      import spark.implicits._
      var valid_operation = true
      val t1 = System.nanoTime
      operation match {
        case "top-country" =>
          //  B.1. print out the top 5 countries by count
          df = spark.sql(
          """SELECT place.country_code AS country, COUNT(*) AS count
              |FROM tweets
              |GROUP BY place.country_code
              |ORDER BY count DESC
              |LIMIT 5""".stripMargin)
          df.show()
        case "top-lang" =>
          //  B.2. print out the top 5 languages by count
          df = spark.sql(
          """SELECT lang, COUNT(*) AS count
              |FROM tweets
              |GROUP BY lang
              |ORDER BY count DESC
              |LIMIT 5""".stripMargin)
          df.show()
        case "top-country-with-lang" =>
          //  B.3. print out the top 5 countries by count, and the top five languages in each of them by percentage
          // B.3.1. start by copying the same query from part B.1, but add `, getTopLangs(collect_list(lang))` before the `FROM` keyword.
          df = spark.sql(
            s"""
               |SELECT place.country_code AS country, COUNT(*) AS count,
               |getTopLangs(collect_list(lang)) AS top_langs
               |FROM tweets
               |GROUP BY place.country_code
               |ORDER BY count DESC
               |LIMIT 5""".stripMargin)
          println("Schema after step B.3.1:")
          df.printSchema()

          //  B.3.2. Use the `explode` function on the `top_lang` column
          println("\nSchema after step B.3.2:")
          df = df.withColumn("top_langs", explode($"top_langs"))
          df.printSchema()
          // Create a view for the new dataframe
          df.createOrReplaceTempView("tweets")

          //  B.3.3. update this command to get the table in the expected output
          df = spark.sql(
            s"""
               |SELECT country AS country, count AS count,
               |top_langs._1 as lang,
               |(top_langs._2 /count) AS lang_percent
               |FROM tweets
               |GROUP BY country, top_langs._1, top_langs._2, count
               |ORDER BY count DESC, lang_percent DESC""".stripMargin)
          println("\nSchema after step B.3.3:")
          df.printSchema()
          df.show(25)

        case "corr" =>
        // B.4. compute the correlation between the `user_followers_count` and `retweet_count`
        println(df.stat.corr("user.statuses_count", "user.followers_count"))

        case "top-hashtags" =>
        // TODO: B.5. Get the hashtags with the most tweets
        // B.5.1. explode the hashtags columns
          df = df.withColumn("hashtags", explode($"hashtags"))
        // B.5.2. create a view for the new dataframe
          df.createOrReplaceTempView("tweets")
        // B.5.3. use a sql query to get the top 10 hashtags with the most tweets.
          df = spark.sql(
            s"""
               |SELECT hashtags AS hashtags, COUNT(*) AS count
               |FROM tweets
               |GROUP BY hashtags
               |ORDER BY count DESC
               |LIMIT 10
               |""".stripMargin)
        // B.5.4. show the final result
          df.show(10)
        case _ => valid_operation = false
      }
      val t2 = System.nanoTime
      if (valid_operation)
        println(s"Operation $operation on file '$inputfile' finished in ${(t2 - t1) * 1E-9} seconds")
      else
        Console.err.println(s"Invalid operation '$operation'")
    } finally {
      spark.stop
    }
  }
}

