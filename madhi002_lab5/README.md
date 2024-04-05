# Lab 5

## Student information

* Full name: Mrinisha Adhikari
* E-mail: madhi002@ucr.edu
* UCR NetID: madhi002
* Student ID: 862309931

## Answers
*Note* As discussed in lab class, I had to finish this lab as both name node and data node because 2 of my group members were not in class, and one of them couldn't
finish the "in-home" part due to errors. 

* (Q1) Do you think it will use your local cluster? Why or why not?
Yes, I do think it uses the local clusters because the command we passed was for the spark interface, but when opening the web interface after the command, I
saw no new "runnning" or "completed" applications. 

* (Q2) Does the application use the cluster that you started? How did you find out?
Yes, the application did use the cluster that I started. After running the command, I see 1 "completed" application in the spark web interface. The name of the 
completed application specifically states "CS167-Lab5-App-madhi002". I also see my class-130 as the master node and one worker as class-130. 

* (Q3) What is the Spark master printed on the standard output on IntelliJ IDEA?
Using Spark master 'local[*]' 
Number of lines in the log file 30970. 

* (Q4) What is the Spark master printed on the standard output on the terminal?
  Using Spark master 'spark://class-130:7077'
  Number of lines in the log file 30970
I had to use command: spark-submit --class edu.ucr.cs.cs167.madhi002.App madhi002_lab5-1.0-SNAPSHOT.jar hdfs://class-130:9000/user/cs167/nasa_19950801_madhi002.tsv due to file not found error with hdfs:///. 

* (Q5) For the previous command that prints the number of matching lines, how many tasks were created, and how much time it took to process each task.
Two tasks were created (TID:0,1), one took 1641 ms and another took 1680 ms:
  16:05:08.158 [task-result-getter-1] INFO  org.apache.spark.scheduler.TaskSetManager - Finished task 1.0 in stage 0.0 (TID 1) in 1641 ms on 169.235.28.130 (executor 0) (1/2)
  16:05:08.175 [task-result-getter-0] INFO  org.apache.spark.scheduler.TaskSetManager - Finished task 0.0 in stage 0.0 (TID 0) in 1680 ms on 169.235.28.130 (executor 0) (2/2)

* (Q6) For the previous command that counts the lines and prints the output, how many tasks in total were generated?
Four tasks were created (TID:0,1,2,3):
  17:03:25.499 [task-result-getter-0] INFO  org.apache.spark.scheduler.TaskSetManager - Finished task 1.0 in stage 0.0 (TID 1) in 1476 ms on 169.235.28.130 (executor 0) (1/2)
  17:03:25.503 [task-result-getter-1] INFO  org.apache.spark.scheduler.TaskSetManager - Finished task 0.0 in stage 0.0 (TID 0) in 1502 ms on 169.235.28.130 (executor 0) (2/2)
  17:03:26.085 [task-result-getter-3] INFO  org.apache.spark.scheduler.TaskSetManager - Finished task 1.0 in stage 1.0 (TID 3) in 443 ms on 169.235.28.130 (executor 0) (1/2)
  17:03:26.086 [task-result-getter-2] INFO  org.apache.spark.scheduler.TaskSetManager - Finished task 0.0 in stage 1.0 (TID 2) in 445 ms on 169.235.28.130 (executor 0) (2/2)

* (Q7) Compare this number to the one you got earlier.
The number of tasks created exactly double. The number of tasks created in Q5 was 2. The number tasks in Q6 was exactly the double of Q5: 4.  

* (Q8) Explain why we get these numbers.
The file that we input is read twice, or in two stages. First task in stage '0'': TID 1, and then file is read again in stage '0' in : TID 3. Specifically seen here:
  Finished task 1.0 in stage 0.0 (TID 1) in 1476 ms on 169.235.28.130 (executor 0) (1/2)
  Finished task 0.0 in stage 0.0 (TID 0) in 1502 ms on 169.235.28.130 (executor 0) (2/2)
  Finished task 1.0 in stage 1.0 (TID 3) in 443 ms on 169.235.28.130 (executor 0) (1/2)
  Finished task 0.0 in stage 1.0 (TID 2) in 445 ms on 169.235.28.130 (executor 0) (2/2)

* (Q9) What can you do to the current code to ensure that the file is read only once?
We use the cache function so that the default storage level is the memory. We can try by editing a specific logfile line Like so: public JavaRDD<String> logFile = spark.textFile(inputPath).cache();

* (Q10) How many stages does your program have, and what are the steps in each stage?
In the DAG Visualization section, I see a graph with 2 stages: Stage 0 and Stage 1. Within Stage 0, the three steps are: TextFile -> Map -> countByKey.
Within Stage 1, there is only one step: countByKey. 

* (Q11) Why does your program have two stages?
My program has two stages because of 'countByKey' and the shuffle operation it induces. Particularly, Stage 0 performs a map operation along with partial 
aggregation within a given partition. Then Stage 1 involves shuffles the data across partitions for final aggregation, which is needed due to update the count
of each key globally. 
