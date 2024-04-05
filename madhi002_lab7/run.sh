mvn clean package

spark-submit --master "local[*]" --class edu.ucr.cs.cs167.madhi002.PreprocessTweets ./target/madhi002_lab7-1.0-SNAPSHOT.jar ./Tweets_1m.json
spark-submit --master "local[*]" --class edu.ucr.cs.cs167.madhi002.AnalyzeTweets ./target/madhi002_lab7-1.0-SNAPSHOT.jar top-country ./Tweets_1m.json
spark-submit --master "local[*]" --class edu.ucr.cs.cs167.madhi002.AnalyzeTweets ./target/madhi002_lab7-1.0-SNAPSHOT.jar top-country ./tweets.json
spark-submit --master "local[*]" --class edu.ucr.cs.cs167.madhi002.AnalyzeTweets ./target/madhi002_lab7-1.0-SNAPSHOT.jar top-country ./tweets.parquet
spark-submit --master "local[*]" --class edu.ucr.cs.cs167.madhi002.AnalyzeTweets ./target/madhi002_lab7-1.0-SNAPSHOT.jar top-lang ./Tweets_1m.json
spark-submit --master "local[*]" --class edu.ucr.cs.cs167.madhi002.AnalyzeTweets ./target/madhi002_lab7-1.0-SNAPSHOT.jar top-lang ./tweets.json
spark-submit --master "local[*]" --class edu.ucr.cs.cs167.madhi002.AnalyzeTweets ./target/madhi002_lab7-1.0-SNAPSHOT.jar top-lang ./tweets.parquet
spark-submit --master "local[*]" --class edu.ucr.cs.cs167.madhi002.AnalyzeTweets ./target/madhi002_lab7-1.0-SNAPSHOT.jar top-country-with-lang ./Tweets_1m.json
spark-submit --master "local[*]" --class edu.ucr.cs.cs167.madhi002.AnalyzeTweets ./target/madhi002_lab7-1.0-SNAPSHOT.jar top-country-with-lang ./tweets.json
spark-submit --master "local[*]" --class edu.ucr.cs.cs167.madhi002.AnalyzeTweets ./target/madhi002_lab7-1.0-SNAPSHOT.jar top-country-with-lang ./tweets.parquet
spark-submit --master "local[*]" --class edu.ucr.cs.cs167.madhi002.AnalyzeTweets ./target/madhi002_lab7-1.0-SNAPSHOT.jar corr ./Tweets_1m.json
spark-submit --master "local[*]" --class edu.ucr.cs.cs167.madhi002.AnalyzeTweets ./target/madhi002_lab7-1.0-SNAPSHOT.jar corr ./tweets.json
spark-submit --master "local[*]" --class edu.ucr.cs.cs167.madhi002.AnalyzeTweets ./target/madhi002_lab7-1.0-SNAPSHOT.jar corr ./tweets.parquet
spark-submit --master "local[*]" --class edu.ucr.cs.cs167.madhi002.AnalyzeTweets ./target/madhi002_lab7-1.0-SNAPSHOT.jar top-hashtags ./tweets.json
spark-submit --master "local[*]" --class edu.ucr.cs.cs167.madhi002.AnalyzeTweets ./target/madhi002_lab7-1.0-SNAPSHOT.jar top-hashtags ./tweets.parquet
