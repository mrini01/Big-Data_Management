# Lab 5

## Student information

* Full name: Mrinisha Adhikari
* E-mail: madhi002@ucr.edu
* UCR NetID: madhi002
* Student ID: 862309931

## Answers

* (Q1) What is the nesting level of this column `root.entities.hashtags.element.text`?
The nesting level of `root.entities.hashtags.element.text` is 3. It starts as 'root' which is given to us as level 0, then goes into 'entities' which would be level 1, 
then into 'hashtags' which would be level 2, then into 'text' which is level 3. Here, 'element' is given as representing an element in an array and doesn't add to the 
nesting levels.

* (Q2) In Parquet, would this field be stored as a repeated column? Explain your answer.
Yes, this would be stored as a repeated column in parquet.Parquet does support nested data structures such as arrays and structs.  In the schema we can see that the 'hastags' is an array of structs,
where a given struct (an 'element') has a 'text' field and more. So, in parquet the 'text' field within each 'element' of the array of 'hashtags' would be the repeated field since it can occur multiple 
times in a given tweet's 'entities' struct. 

* (Q3) Based on this schema answer the following:***

    - How many fields does the `place` column contain?
    8 fields in the 'place' column, which includes: 'bounding_box', 'country', 'country_code', 'full_name', 'id', 'name', 'place_type', and 'url'.
    - How many fields does the `user` column contain?
    39 fields in the 'user' column, they start from 'contributions_enabled' and end at 'verified'. 
    - What is the datatype of the `time` column?
    The datatype of 'time' is string.
    - What is the datatype of the `hashtags` column?
    The datatype of 'hashtags' is an array of structs, which includes two fields: 'indices'(long) and 'text'(string).

* (Q4) Based on this new schema answer the following:
    - *How many fields does the `place` column contain?*
    Only 3 fields in the 'place' column now, which includes: 'country_code', 'name', and 'place_type'.
    - *How many fields does the `user` column contain?*
    Only 3 fields in the 'user' column now, which includes: 'followers_count', 'statuses_count', and 'id'.
    - *What is the datatype of the `time` column?*
    The datatype of 'time' is now timestamp (long).
    - *What is the datatype of the `hashtags` column?*
    The datatype of 'hashtags' is now an array of strings. 
  
* (Q5) What is the size of each folder? Explain the difference in size, knowing that the two folders `tweets.json` and `tweets.parquet` contain the exact same dataframe?
Folder 'tweets.json' is: 311.7 MiB, and folder 'tweets.parquet' is: 105.4 MiB.
The big size gap between the two files exists because parquet format is a lot more efficient than JSON. Specifically, parquet is a columnar storage format that prioritizes space efficiency and 
compression and encoding schemes. While, JSON is a row-based storage format without particular space or compression optimizations. This is why despite using the exact same dataframe, the parquet file is
significantly smaller than the JSON. 

* (Q6) What is the error that you see? Why isn't Spark able to write this dataframe in the CSV format?
Error message:
  Exception in thread "main" org.apache.spark.sql.AnalysisException: [UNSUPPORTED_DATA_TYPE_FOR_DATASOURCE] The CSV datasource doesn't support the column `coordinates` of the type "STRUCT<coordinates: ARRAY<DOUBLE>, type: STRING>".

The error occurred because CSV files are not formatted for complex or nested data structures. The error specifically points out that the column 'coordinates' is of the type 'STRUCT<coordinates: ARRAY<DOUBLE>, type: STRING>' which
is not supported by the CSV datasource. It can only organize simple data, and is not meant for hierarchical or multidimensional data. 

* (Q7.1) What do you see in the output? Copy it here.
All three files produce the same output:
  +-------+------+
  |country| count|
  +-------+------+
  |     US|196740|
  |     JP|133690|
  |     GB| 65130|
  |     PH| 57320|
  |     BR| 44570|
  +-------+------+

* (Q7.2) What do you observe in terms of run time for each file? Which file is slowest and which is the fastest? Explain your observation?.
Tweets_1m.json: Operation top-country on file './Tweets_1m.json' finished in 49.738710358000006 seconds
tweets.json: Operation top-country on file './tweets.json' finished in 16.815882132000002 seconds
tweets.parquet: Operation top-country on file './tweets.parquet' finished in 10.855382861 seconds

As observed, the 'Tweets_1m.json' file is the slowest, taking almost 50 seconds. Then, 'tweets.json' taking about 17 seconds, and the fastest being 'tweets.parquet'. This is because running analyzing queries or computational queries
is the most efficient in column based formats such as parquet files. JSON files are row based and not the best for column based searches. Also, the two smaller 'tweets' files are compressed and smaller in size and easier to read through
than the Tweets_1m file, hence the huge gap. 

* (Q8.1) What are the top languages that you see? Copy the output here.
All three files produce the same output:
  +----+------+
  |lang| count|
  +----+------+
  |  en|384960|
  |  ja|130830|
  | und| 76000|
  |  es| 60420|
  |  in| 47580|
  +----+------+

* (Q8.2) Do you also observe the same perfroamnce for the different file formats?
Tweets_1m.json: Operation top-lang on file './Tweets_1m.json' finished in 32.074704151 seconds
tweets.json: Operation top-lang on file './tweets.json' finished in 11.941780405000001 seconds
tweets.parquet: Operation top-lang on file './tweets.parquet' finished in 8.840161021 seconds

Yes, I observed similar performance for the different file formats. 'Tweets_1m' took the longest, followed by 'tweets.json' then 'tweets.parquet'. Once again due to parquet's efficiency as columnar storage file.

* (Q9) After step B.3.2, how did the schema change? What was the effect of the `explode` function?
The 'explode' function breaks the array column, and creates a new record that attaches each value of the array element. To show this, we can compare the output schema from B.3.1 and B.3.2 that I got:
  Schema after step B.3.1:
  root
  |-- country: string (nullable = true)
  |-- count: long (nullable = false)
  |-- top_langs: array (nullable = true)
  |    |-- element: struct (containsNull = true)
  |    |    |-- _1: string (nullable = true)
  |    |    |-- _2: integer (nullable = false)


Schema after step B.3.2:
root
|-- country: string (nullable = true)
|-- count: long (nullable = false)
|-- top_langs: struct (nullable = true)
|    |-- _1: string (nullable = true)
|    |-- _2: integer (nullable = false)

So form my outputs, in schema B.3.1, we see: |-- top_langs: array (nullable = true). After we apply 'explode' on top_langs in B.3.2, we see: |-- top_langs: struct (nullable = true). Meaning, the array based column was broken down
creating new record for each element. 

* (Q10) For the country with the most tweets, what is the fifth most used language? Also, copy the entire output table here.
The US has the most tweets, and the fifth most used language used in it seems to be 'ja'(japanese). This is my Complete output table:
  +-------+------+----+--------------------+
  |country| count|lang|        lang_percent|
  +-------+------+----+--------------------+
  |     US|196740|  en|  0.8595100132154112|
  |     US|196740| und| 0.08661177188167124|
  |     US|196740|  es|0.013977838771983329|
  |     US|196740|  tl|0.006811019619802785|
  |     US|196740|  ja|0.004269594388533089|
  |     JP|133690|  ja|  0.9513052584336898|
  |     JP|133690| und| 0.01668037998354402|
  |     JP|133690|  en|0.015558381329942405|
  |     JP|133690|  in|0.005011593986087217|
  |     JP|133690|  th|0.002019597576482...|
  |     GB| 65130|  en|  0.8922155688622755|
  |     GB| 65130| und| 0.07032089666820206|
  |     GB| 65130|  es|0.004606172270842929|
  |     GB| 65130|  fr|0.004452633195148165|
  |     GB| 65130|  ar|0.003992015968063872|
  |     PH| 57320|  tl|  0.5087229588276343|
  |     PH| 57320|  en|   0.349092812281926|
  |     PH| 57320| und| 0.06856245638520586|
  |     PH| 57320|  in|0.028087927424982555|
  |     PH| 57320|  es|0.008199581297976273|
  |     BR| 44570|  pt|  0.8761498765986089|
  |     BR| 44570| und| 0.04711689477226834|
  |     BR| 44570|  en| 0.03006506618801885|
  |     BR| 44570|  es|0.021539151895894098|
  |     BR| 44570|  it|0.005160421808391295|
  +-------+------+----+--------------------+

* (Q11) Does the observed statistical value show a strong correlation between the two columns? Note: a value close to 1 or -1 means there is high correlation, but a value that is close to 0 means there is no correlation.
There seems to be low/no correlation, as the values i got is closer to 0. The statistical values i got for the files:
Tweets_1m.json: 0.05958391887410892
tweets.json: 0.05958391887410934
tweets.parquet: 0.05958391887410891 


* (Q12.1) What are the top 10 hashtags? Copy paste your output here.
Top 10 hashtags output table:
  +-------------------+-----+
  |           hashtags|count|
  +-------------------+-----+
  |     ALDUBxEBLoveis| 4110|
  |                job| 2730|
  |             trndnl| 2420|
  |ShowtimeLetsCelebr8| 2200|
  |             Hiring| 2080|
  |              no309| 1130|
  |              LalOn| 1130|
  |       FurkanPalalı| 1130|
  |            sbhawks| 1050|
  |             hiring| 1000|
  +-------------------+-----+

* (Q12.2) For this operation, do you observe difference in performance when comparing the two different input files `tweets.json` and `tweets.parquet`? Explain the reason behind the difference.
tweets.json: Operation top-hashtags on file './tweets.json' finished in 15.129603402 seconds
tweets.parquet: Operation top-hashtags on file './tweets.parquet' finished in 11.385788159 seconds

Once again, 'tweets.parquet' is faster than 'tweets.json' because columnar storage format like parquet is more efficient for aggregation queries like counting hashtags. 

* Submission Table:
  | Command               | Tweets_1m.json | tweets.json | tweets.parquet |
  |:---------------------:|:---------------|:-----------:|:--------------:|
  | top-country           | 49.738710358000006 | 16.815882132000002 | 10.855382861 |
  | top-lang              | 32.074704151 | 11.941780405000001 | 8.840161021 |
  | top-country-with-lang | 55.36239242400001 | 21.448266587000003 | 16.614987968 |
  | corr                  | 48.426847704000004 | 13.583645462000002 | 7.311250549  |
  | top-hashtags          |  N/A           | 15.129603402 | 11.385788159 |