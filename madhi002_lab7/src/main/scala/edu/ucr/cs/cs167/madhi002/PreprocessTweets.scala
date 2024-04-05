package edu.ucr.cs.cs167.madhi002

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf

import scala.collection.mutable


object PreprocessTweets {
  def main(args: Array[String]): Unit = {
    val inputfile: String = args(0)
    val outputfile: String = "tweets"

    val getHashtagTexts: UserDefinedFunction = udf((x: mutable.WrappedArray[GenericRowWithSchema]) => {
      x match {
        case x: mutable.WrappedArray[GenericRowWithSchema] => x.map(_.getAs[String]("text"))
        case _ => null
      }
    })

    val conf = new SparkConf
    if (!conf.contains("spark.master"))
      conf.setMaster("local[*]")
    println(s"Using Spark master '${conf.get("spark.master")}'")

    val spark = SparkSession
      .builder()
      .appName("CS167 Lab7 - Preprocessor")
      .config(conf)
      .getOrCreate()
    spark.udf.register("getHashtagTexts", getHashtagTexts)

    try {
      import spark.implicits._
      //  A.1. read file and print schema
      //           A.1.1 read json file
      var df = spark.read.json(inputfile)
      //           A.1.2 print dataframe schema
      df.printSchema()

      //  A.2. use SQL to select relevant columns
      //           A.2.1 createOrReplaceTempView
      df.createOrReplaceTempView("tweets")
      //           A.2.2 use Spark SQL select query
      df = spark.sql(
        """SELECT
       id,
       text,
       place,
       user,
       timestamp_ms AS time,
       entities.hashtags AS hashtags,
       lang
       FROM tweets""")
      //           A.2.3 print schema of new dataframe
      df.printSchema()

      //  A.3. apply functions to some columns, by modifying the previous SQL command as follows:
      //           A.3.1 drop some nested columns from `place`
      //           A.3.2 drop some nested columns `user`
      //           A.3.3 transform `timestamp` to the appropriate datatype
      //           A.3.4 simplify the structure of the `hashtags` column
      // A.3.1-A.3.4 combined into one query
      df = spark.sql(
        """SELECT
       id,
       text,
       (place.country_code AS country_code, place.name AS name, place.place_type AS place_type) AS place,
       (user.followers_count AS followers_count, user.statuses_count AS statuses_count, user.id AS id) AS user,
       timestamp(cast(timestamp_ms as long)/1000) AS time,
       getHashtagTexts(entities.hashtags) AS hashtags,
       lang
       FROM tweets""")
      //           A.3.5 print schema of new dataframe
      df.printSchema()

      //  A.5. show the dataframe
      df.show()

      //  A.6. save the dataframe in JSON format
      df.write.mode("overwrite").json(outputfile + ".json")

      //  A.7. save the file in Parquet format
      df.write.mode("overwrite").parquet(outputfile + ".parquet")

      //  A.8. save the file in CSV format
      //df.write.mode("overwrite").csv(outputfile + ".csv")
    } finally {
      spark.stop
    }
  }
}
