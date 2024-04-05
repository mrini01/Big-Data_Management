# Lab 1

## Student information
* Full name: Mrinisha Adhikari
* E-mail: madhi002@ucr.edu
* UCR NetID: madhi002
* Student ID: 862309931

## Answers

* (Q1) What is the name of the created directory?
madhi002_lab1

* (Q2) What do you see at the console output?
Hello World!

* (Q3) What do you see at the output?

An error pops up that reads:
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/home/cs167/.m2/repository/org/slf4j/slf4j-reload4j/1.7.36/slf4j-reload4j-1.7.36.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/home/cs167/.m2/repository/org/slf4j/slf4j-log4j12/1.7.25/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Reload4jLoggerFactory]
log4j:WARN No appenders could be found for logger (org.apache.hadoop.metrics2.lib.MutableMetricsFactory).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 0
	at edu.ucr.cs.cs167.madhi002.App.main(App.java:60)

Process finished with exit code 1

* (Q4) What is the output that you see at the console?

So, an output.txt folder is created with 4 files: ._Success.crc, .part-r-00000.crc, _SUCCESS, part-r-00000

The text output of part-r-00000 is as so:

but	1
cannot	3
crawl	1
do	1
fly,	1
forward	1
have	1
if	3
keep	1
moving	1
run	1
run,	1
then	3
to	1
walk	1
walk,	1
whatever	1
you	5

In the console specifically, the "Exception thread" error is now gone, and it just shows warnings:
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/home/cs167/.m2/repository/org/slf4j/slf4j-reload4j/1.7.36/slf4j-reload4j-1.7.36.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/home/cs167/.m2/repository/org/slf4j/slf4j-log4j12/1.7.25/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Reload4jLoggerFactory]
log4j:WARN No appenders could be found for logger (org.apache.hadoop.metrics2.lib.MutableMetricsFactory).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.

Process finished with exit code 0

* (Q5) Does it run? Why or why not?

Using: java -cp target/madhi002_lab1-1.0-SNAPSHOT.jar edu.ucr.cs.cs167.madhi002.App. There is an error: 

Exception in thread "main" java.lang.NoClassDefFoundError: org/apache/hadoop/conf/Configuration
	at edu.ucr.cs.cs167.madhi002.App.main(App.java:52)
Caused by: java.lang.ClassNotFoundException: org.apache.hadoop.conf.Configuration
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:581)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:522)
	... 1 more

Using:  hadoop jar target/madhi002_lab1-1.0-SNAPSHOT.jar edu.ucr.cs.cs167.madhi002.App input.txt output.txt. Runs the program fine. 
For debugging, deleting the already created output file, gives us a run, a (condensed) output in the console is as so:

2024-01-12 20:42:25,503 INFO impl.MetricsConfig: Loaded properties from hadoop-metrics2.properties
2024-01-12 20:42:25,621 INFO impl.MetricsSystemImpl: Scheduled Metric snapshot period at 10 second(s).
2024-01-12 20:42:25,621 INFO impl.MetricsSystemImpl: JobTracker metrics system started
..
...
....
Map-Reduce Framework
		Map input records=4
		Map output records=28
		Map output bytes=252
		Map output materialized bytes=208
		Input split bytes=115
		Combine input records=28
		Combine output records=18
		Reduce input groups=18
		Reduce shuffle bytes=208
		Reduce input records=18
		Reduce output records=18
		Spilled Records=36
		Shuffled Maps =1
		Failed Shuffles=0
		Merged Map outputs=1
		GC time elapsed (ms)=49
		Total committed heap usage (bytes)=465313792
	Shuffle Errors
		BAD_ID=0
		CONNECTION=0
		IO_ERROR=0
		WRONG_LENGTH=0
		WRONG_MAP=0
		WRONG_REDUCE=0
	File Input Format Counters 
		Bytes Read=139
	File Output Format Counters 
		Bytes Written=142
