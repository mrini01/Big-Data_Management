mvn clean package
spark-submit --class edu.ucr.cs.cs167.madhi002.Filter target/madhi002_lab5-1.0-SNAPSHOT.jar nasa_19950630.22-19950728.12.tsv filter_output 302
spark-submit --class edu.ucr.cs.cs167.madhi002.Aggregation target/madhi002_lab5-1.0-SNAPSHOT.jar nasa_19950630.22-19950728.12.tsv aggregation_output 302
