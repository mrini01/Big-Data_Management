# Lab 6

## Student information

* Full name: Mrinisha Adhikari
* E-mail: madhi002@ucr.edu
* UCR NetID: madhi002
* Student ID: 862309931

## Answers

* (Q1) What are these two arguments?
The two arguments are: 'command' and 'inputfile'. Which basically means one argument as  the command line for a given test case, and another argument as one of the 2 input files we downloaded.

* (Q2) If you do this bonus part, copy and paste your code in the README file as an answer to this question.

* (Q3) What is the type of the attributes `time` and `bytes` this time? Why?
Both 'time' and 'bytes' are now string attributes. This is because Spark no longer "infers" the schema based on the values in the file, since we commented out "option("inferSchema", "true")" command. 

* (Q4) If you do this bonus part, copy and paste your code in the README file as an aswer to this question.
(B) I chose to do part 16c, my SQL query separately aggregates the counts of the log entries for each given response code. Then, partitions further to see if occurred before or after the given timestamp.
SQL query code:
  val query: String =
  s"""
  |SELECT response AS code,
  |SUM(CASE WHEN time < $filterTimestamp THEN 1 ELSE 0 END) AS CountBefore,
  |SUM(CASE WHEN time >= $filterTimestamp THEN 1 ELSE 0 END) AS CountAfter
  |FROM log_lines
  |GROUP BY response
  |ORDER BY response
  |""".stripMargin