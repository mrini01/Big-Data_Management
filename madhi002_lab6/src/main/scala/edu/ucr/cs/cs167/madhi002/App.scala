package edu.ucr.cs.cs167.madhi002

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.Map

object App {

  def main(args: Array[String]) {
    val command: String = args(0)
    val inputfile: String = args(1)

    val conf = new SparkConf
    if (!conf.contains("spark.master"))
      conf.setMaster("local[*]")
    println(s"Using Spark master '${conf.get("spark.master")}'")
    conf.setAppName("CS167_Lab6_App")
    val sparkContext = new SparkContext(conf)
    try {
      val inputRDD: RDD[String] = sparkContext.textFile(inputfile)
      // TODO 1a: filter lines which do not start with "host\tlogname" from `inputRDD`
      val validLines: RDD[String] = inputRDD.filter(x => !x.startsWith("host\tlogname"))
      // TODO 1b: split each line by "\t" from `validLines` via `map`
      val parsedLines: RDD[Array[String]] = validLines.map(s => s.split("\t"))
      val t1 = System.nanoTime
      var valid_command = true
      command match {
        case "count-all" =>
          // Count total number of records in the file
          // TODO 2: count total number of records in the file on `parsedLines`
          val count: Long = parsedLines.count()
            println(s"Total count for file '$inputfile' is $count")
        case "code-filter" =>
          // Filter the file by response code, args(2), and print the total number of matching lines
          val responseCode: String = args(2)
          // TODO 3: `filter` on `parsedLines` by `responseCode`
          val filteredLines: RDD[Array[String]] = parsedLines.filter(line => line(5) == responseCode)
          val count: Long = filteredLines.count()
          println(s"Total count for file '$inputfile' with response code $responseCode is $count")
        case "time-filter" =>
          // Filter by time range [from = args(2), to = args(3)], and print the total number of matching lines
          val from: Long = args(2).toLong
          val to: Long = args(3).toLong
          // TODO 4: `filter` on `parsedLines` by time (column 2) with `from` and `to`
          val filteredLines: RDD[Array[String]] = parsedLines.filter(line => line(2).toLong>=from && line(2).toLong<=to)
          val count: Long = filteredLines.count()
          println(s"Total count for file '$inputfile' in time range [$from, $to] is $count")
        case "count-by-code" =>
          // Group the lines by response code and count the number of records per group
          // TODO 5a: `map` on `parsedLines` by response code (column 5)
          val loglinesByCode: RDD[(String, Long)] = parsedLines.map(line => (line(5),1))
          // TODO 5b: `countByKey` on `loglinesByCode`
          val counts: Map[String, Long] = loglinesByCode.countByKey()
            println(s"Number of lines per code for the file '$inputfile'")
          println("Code,Count")
          counts.toSeq.sortBy(_._1).foreach(pair => println(s"${pair._1},${pair._2}"))
        case "sum-bytes-by-code" =>
          // Group the lines by response code and sum the total bytes per group
          // TODO 6a: `map` on `parsedLines` by response code (column 5) and bytes (column 6)
          val loglinesByCode: RDD[(String, Long)] = parsedLines.map(line => (line(5), line(6).toLong))
          // TODO 6b: `reduceByKey` on `loglinesByCode`
          val sums: RDD[(String, Long)] = loglinesByCode.reduceByKey(_ + _)
            println(s"Total bytes per code for the file '$inputfile'")
          println("Code,Sum(bytes)")
          sums.sortByKey().collect().foreach(pair => println(s"${pair._1},${pair._2}"))
        case "avg-bytes-by-code" =>
          // Group the liens by response code and calculate the average bytes per group
          // TODO 7a: `map` on `parsedLines` by response code (column 5) and bytes (column 6)
          val loglinesByCode: RDD[(String, Long)] = parsedLines.map(line => (line(5), line(6).toLong))
          // TODO 7b: `reduceByKey` on `loglinesByCode`
          val sums: RDD[(String, Long)] = loglinesByCode.reduceByKey(_ + _)
          // TODO 7c: `countByKey` on `loglinesByCode`
          val counts: Map[String, Long] = loglinesByCode.countByKey()
            println(s"Average bytes per code for the file '$inputfile'")
          println("Code,Avg(bytes)")
          sums.sortByKey().collect().foreach(pair => {
            val code = pair._1
            val sum = pair._2
            val count = counts(code)
            println(s"$code,${sum.toDouble / count}")
          })
        // TODO 7d: replace the above codes for bonus with `aggregateByKey`
        case "top-host" =>
          // Print the host the largest number of lines and print the number of lines
          // TODO 8a: `map` on `parsedLines` by host (column 0)
          val loglinesByHost: RDD[(String, Long)] = parsedLines.map(line => (line(0),1))
          // TODO 8b: `reduceByKey` on `loglinesByHost`
          val counts: RDD[(String, Long)] = loglinesByHost.reduceByKey(_+_)
          // TODO 8c: `sortBy` on `counts`
          val sorted: RDD[(String, Long)] = counts.sortBy(_._2,ascending = false)
          // TODO 8d: `first` on `sorted`
          val topHost: (String, Long) = sorted.first()
            println(s"Top host in the file '$inputfile' by number of entries")
          println(s"Host: ${topHost._1}")
          println(s"Number of entries: ${topHost._2}")
        case _ => valid_command = false
      }
      val t2 = System.nanoTime
      if (valid_command)
        println(s"Command '$command' on file '$inputfile' finished in ${(t2 - t1) * 1E-9} seconds")
      else
        Console.err.println(s"Invalid command '$command'")
    } finally {
      sparkContext.stop
    }
  }
}
