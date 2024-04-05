package edu.ucr.cs.cs167.groupC1;

import edu.ucr.cs.bdlab.beast.JavaSpatialSparkContext;
import org.apache.spark.beast.CRSServer;
import org.apache.spark.beast.SparkSQLRegistration;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

public class BeastJava {
  public static void main(String[] args) {
    // Initialize Spark
    SparkConf conf = new SparkConf().setAppName("Beast Example");

    // Set Spark master to local if not already set
    if (!conf.contains("spark.master"))
      conf.setMaster("local[*]");

    // Create Spark session (for Dataframe API) and Spark context (for RDD API)
    SparkSession sparkSession = SparkSession.builder().config(conf).getOrCreate();
    JavaSpatialSparkContext sparkContext = new JavaSpatialSparkContext(sparkSession.sparkContext());
    // Start the CRSServer and store the information in the SparkContext.
    CRSServer.startServer(sparkContext);
    SparkSQLRegistration.registerUDT();
    SparkSQLRegistration.registerUDF(sparkSession);

    try {
      // TODO Insert your code here
    } finally {
      // Clean up Spark session
      sparkSession.stop();
    }
  }
}
