package edu.ucr.cs.cs167.madhi002

import org.apache.spark.{SparkConf, sql}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object AppSQL {

  def main(args: Array[String]) {
    val conf = new SparkConf
    if (!conf.contains("spark.master"))
      conf.setMaster("local[*]")
    println(s"Using Spark master '${conf.get("spark.master")}'")

    val spark = SparkSession
      .builder()
      .appName("CS167_Lab6_AppSQL")
      .config(conf)
      .getOrCreate()

    val command: String = args(0)
    val inputfile: String = args(1)
    try {
      val input = spark.read.format("csv")
        .option("sep", "\t")
        .option("inferSchema", "true")
        .option("header", "true")
        .load(inputfile)
      import spark.implicits._
      input.createOrReplaceTempView("log_lines")
      val t1 = System.nanoTime
      var validCommand = true
      command match {
        case "count-all" =>
          // Count total number of records in the file
          // TODO 9: write SQL query to count the total number of rows in `log_lines`
          val query: String = "SELECT count(*) FROM log_lines;"
          val count: Long = spark.sql(query).first().getAs[Long](0)
          println(s"Total count for file '$inputfile' is $count")
        case "code-filter" =>
          // Filter the file by response code, args(2), and print the total number of matching lines
          val responseCode: String = args(2)
          // TODO 10: write SQL query to filter view `log_lines` by `responseCode`
          val query: String = "SELECT count(*) FROM log_lines WHERE response=" + responseCode + ";"
          val count: Long = spark.sql(query).first().getAs[Long](0)
          println(s"Total count for file '$inputfile' with response code $responseCode is $count")
        case "time-filter" =>
          // Filter by time range [from = args(2), to = args(3)], and print the total number of matching lines
          val from: Long = args(2).toLong
          val to: Long = args(3).toLong
          // TODO 11: write SQL query to filter view `log_lines` by `time` between `from` and `to` --this one---
          val query: String = "SELECT count(*) FROM log_lines WHERE time BETWEEN "  + from +  " AND "  + to + ";"
          val count: Long = spark.sql(query).first().getAs[Long](0)
          println(s"Total count for file '$inputfile' in time range [$from, $to] is $count")
        case "count-by-code" =>
          // Group the lines by response code and count the number of records per group
          println(s"Number of lines per code for the file '$inputfile'")
          println("Code,Count")
          // TODO 12: write SQL query the get the count for each `response`, results must be ordered by `response` in ascending order
          val query: String =
            """
              |SELECT response, COUNT(bytes)
              |FROM log_lines
              |GROUP BY response
              |ORDER BY response
              |""".stripMargin
            spark.sql(query).foreach(row => println(s"${row.get(0)},${row.get(1)}"))
        case "sum-bytes-by-code" =>
          // Group the lines by response code and sum the total bytes per group
          println(s"Total bytes per code for the file '$inputfile'")
          println("Code,Sum(bytes)")
          // TODO 13: write SQL query the get the sum of `bytes` for each `response`, results must be ordered by `response` in ascending order
          val query: String =
            """
              |SELECT response, SUM(bytes)
              |FROM log_lines
              |GROUP BY response
              |ORDER BY response
              |""".stripMargin
            spark.sql(query).foreach(row => println(s"${row.get(0)},${row.get(1)}"))
        case "avg-bytes-by-code" =>
          // Group the liens by response code and calculate the average bytes per group
          println(s"Average bytes per code for the file '$inputfile'")
          println("Code,Avg(bytes)")
          // TODO 14: write SQL query the get the average of `bytes` for each `response`, results must be ordered by `response` in ascending order
          val query: String =
            """
              |SELECT response, AVG(bytes)
              |FROM log_lines
              |GROUP BY response
              |ORDER BY response
              |""".stripMargin
            spark.sql(query).foreach(row => println(s"${row.get(0)},${row.get(1)}"))
        case "top-host" =>
          // print the host the largest number of lines and print the number of lines
          println(s"Top host in the file '$inputfile' by number of entries")
          // TODO 15: write SQL query the get the `host` with the largest count
          val query: String =
            """
              |SELECT host, COUNT(*) AS cnt
              |from log_lines
              |GROUP BY host
              |ORDER BY cnt DESC
              |LIMIT 1
              |""".stripMargin
          val topHost: Row = spark.sql(query).first()
          println(s"Host: ${topHost.get(0)}")
          println(s"Number of entries: ${topHost.get(1)}")
        case "comparison" =>
          // Given a specific time, calculate the number of lines per response code for the
          // entries that happened before that time, and once more for the lines that happened at or after
          // that time. Print them side-by-side in a tabular form.

          // TODO 16a: comment the following line
          //println("Not implemented")

        // TODO 16b: uncomment the following 3 lines
         val filterTimestamp: Long = args(2).toLong
         println(s"Comparison of the number of lines per code before and after $filterTimestamp on file '$inputfile'")
         println("Code,CountBefore,CountAfter")

        // TODO 16c: Uncomment the following lines and complete your function here if you prefer to use SQL queries
        // Write your query here. Results must be ordered by `response` in ascending order
         val query: String =
        s"""
          |SELECT response AS code,
          |SUM(CASE WHEN time < $filterTimestamp THEN 1 ELSE 0 END) AS CountBefore,
          |SUM(CASE WHEN time >= $filterTimestamp THEN 1 ELSE 0 END) AS CountAfter
          |FROM log_lines
          |GROUP BY response
          |ORDER BY response
          |""".stripMargin
         spark.sql(query).foreach(row => println(s"${row.get(0)},${row.get(1)},${row.get(2)}"))

        // TODO 16d: Uncomment the following lines and complete your function here if you prefer to use DataFrame
        // val countsBefore: DataFrame = // on `input`
        // val countsAfter: DataFrame = // on `input`
        // val comparedResults: DataFrame = countsBefore.join(countsAfter, "response").orderBy("response")
        // comparedResults.foreach(row => println(s"${row.get(0)},${row.get(1)},${row.get(2)}"))
        case _ => validCommand = false
      }
      val t2 = System.nanoTime
      if (validCommand)
        println(s"Command '$command' on file '$inputfile' finished in ${(t2 - t1) * 1E-9} seconds")
      else
        Console.err.println(s"Invalid command '$command'")
    } finally {
      spark.stop
    }
  }
}
