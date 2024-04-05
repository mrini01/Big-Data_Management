package edu.ucr.cs.cs167.groupC1


import edu.ucr.cs.bdlab.beast.geolite.{Feature, IFeature}
import org.apache.spark.SparkConf
import org.apache.spark.beast.SparkSQLRegistration
import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}


/**
 * Scala examples for Beast
 */
object BeastScala {
  def main(args: Array[String]): Unit = {
    // Initialize Spark context

    val conf = new SparkConf().setAppName("Wildfire Data Analysis")
    // Set Spark master to local if not already set
    if (!conf.contains("spark.master"))
      conf.setMaster("local[*]")


    val spark: SparkSession.Builder = SparkSession.builder().config(conf)
    val sparkSession: SparkSession = spark.getOrCreate()
    val sparkContext = sparkSession.sparkContext
    //CRSServer.startServer(sparkContext)
    SparkSQLRegistration.registerUDT
    SparkSQLRegistration.registerUDF(sparkSession)

    val operation: String = args(0)
    val inputFile: String = args(1)
    try {
      // Import Beast features
      import edu.ucr.cs.bdlab.beast._
      val t1 = System.nanoTime()
      var validOperation = true;

      //TASK 1
      operation match {
        case "task1" =>

          //1 Parse and load the CSV file using the Dataframe API.
          val wildfireDF = sparkSession.read.format("csv").option("delimiter", "\t").option("inferSchema", "true").option("header", "true").load(inputFile)

          //2,3,4,5,6: Keep only the following 13 columns, add geometry, change frp column to double, and cast numericals to double.Cast all numericals to double and save final dataframe as RDD
          val wildfireRDD: SpatialRDD = wildfireDF.selectExpr(
            "x",
            "y",
            "cast(acq_date as string) AS acq_date",
            "double(split(frp,',')[0]) AS frp",
            "acq_time",
            "cast(ELEV_mean as double) AS ELEV_mean",
            "cast(SLP_mean as double) AS SLP_mean",
            "cast(EVT_mean as double) AS EVT_mean",
            "cast(EVH_mean as double) AS EVH_mean",
            "cast(CH_mean as double) AS CH_mean",
            "cast(TEMP_ave as double) AS TEMP_ave",
            "cast(TEMP_min as double) AS TEMP_min",
            "cast(TEMP_max as double) AS TEMP_max",
            "ST_CreatePoint(x,y) AS geometry"
          ).toSpatialRDD

          //7 & 8. Load the County dataset using Beast. Run a spatial join query to find the county of each wildfire.
          val countiesDF = sparkSession.read.format("shapefile").load("tl_2018_us_county.zip")
          val countiesRDD: SpatialRDD = countiesDF.toSpatialRDD
          val wildfireCountyJoin: RDD[(IFeature, IFeature)] = wildfireRDD.spatialJoin(countiesRDD)

          //9 & 10.New attribute "County" via GEOID. Then convert it to dataframe.
          val wildfireCounty: DataFrame = wildfireCountyJoin.map({ case (wildfire, county) => Feature.append(wildfire, county.getAs[String]("GEOID"), "County") })
            .toDataFrame(sparkSession)

          //11. Complete dataframe without geometry column
          val completeDF: DataFrame = wildfireCounty.selectExpr(
          "x",
          "y",
          "cast(acq_date as string) AS acq_date",
          "double(split(frp,',')[0]) AS frp",
          "acq_time",
          "cast(ELEV_mean as double) AS ELEV_mean",
          "cast(SLP_mean as double) AS SLP_mean",
          "cast(EVT_mean as double) AS EVT_mean",
          "cast(EVH_mean as double) AS EVH_mean",
          "cast(CH_mean as double) AS CH_mean",
          "cast(TEMP_ave as double) AS TEMP_ave",
          "cast(TEMP_min as double) AS TEMP_min",
          "cast(TEMP_max as double) AS TEMP_max",
          "County"
          ).drop("geometry")

          //TESTING-
          //completeDF.printSchema()
          //completeDF.show()

          //12. 
          completeDF.write.mode(SaveMode.Overwrite).parquet("wildfiredb_ZIP")

          val t2 = System.nanoTime()
          println(s"Operation '$operation' on file '$inputFile' took ${(t2 - t1) * 1E-9} seconds")
          //finished task 1

        //TASK 2:
        case "task2" =>
          val t3 = System.nanoTime()

      }


    } finally {
      sparkSession.stop()
    }
  }
}