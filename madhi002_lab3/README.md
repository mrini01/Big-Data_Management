# Lab 3

## Student information

* Full name: Mrinisha Adhikari
* E-mail: madhi002@ucr.edu
* UCR NetID: madhi002
* Student ID: 862309931

## Answers

* (Q1) Verify the file size and report the running time.
  Copied 2271210910 bytes from 'file:/home/cs167/AREAWATER_madhi002.csv' to 'file:/home/cs167/output.csv' in 4.513448 seconds

* (Q2) Report the running time of the copy command.
  real    0m1.931s
  user    0m0.000s
  sys    0m1.930s

* (Q3) How do the two numbers compare? (The running times of copying the file through your program and the operating system.) Explain IN YOUR OWN WORDS why you see these results.
  The OS is a lot faster than my code. The time was halved in the time taken by the OS system. My program took about 4.5 seconds, while the OS only took 1.9 seconds. This might
  be because my program isn't exactly optimized and uses

* (Q4) Copy the output of this command. (hdfs dfsadmin -report)
  Configured Capacity: 207929917440 (193.65 GB)
  Present Capacity: 200996335616 (187.19 GB)
  DFS Remaining: 200996311040 (187.19 GB)
  DFS Used: 24576 (24 KB)
  DFS Used%: 0.00%
  Replicated Blocks:
  Under replicated blocks: 0
  Blocks with corrupt replicas: 0
  Missing blocks: 0
  Missing blocks (with replication factor 1): 0
  Low redundancy blocks with highest priority to recover: 0
  Pending deletion blocks: 0
  Erasure Coded Block Groups:
  Low redundancy block groups: 0
  Block groups with corrupt internal blocks: 0
  Missing block groups: 0
  Low redundancy blocks with highest priority to recover: 0
  Pending deletion blocks: 0

-------------------------------------------------
Live datanodes (1):

Name: 169.235.28.182:9866 (class-182.cs.ucr.edu)
Hostname: class-182.cs.ucr.edu
Decommission Status : Normal
Configured Capacity: 207929917440 (193.65 GB)
DFS Used: 24576 (24 KB)
Non DFS Used: 6916804608 (6.44 GB)
DFS Remaining: 200996311040 (187.19 GB)
DFS Used%: 0.00%
DFS Remaining%: 96.67%
Configured Cache Capacity: 0 (0 B)
Cache Used: 0 (0 B)
Cache Remaining: 0 (0 B)
Cache Used%: 100.00%
Cache Remaining%: 0.00%
Xceivers: 0
Last contact: Tue Jan 23 17:44:05 PST 2024
Last Block Report: Tue Jan 23 17:43:02 PST 2024
Num of Blocks: 0

* (Q5) What is the total capacity of this cluster and how much of it is free? and how many live data nodes are there?
  Total capacity : 193.65 GB
  Live nodes : 1 (other two group members were not present in lab)

* (Q6) What is the output of this command? (hdfs dfs -ls)
  Found 2 items
  -rw-r--r--   3 cs167 supergroup          0 2024-01-23 18:15 madhi002.txt
  -rw-r--r--   3 cs167 supergroup          3 2024-01-23 18:22 tliu222.txt

* (Q7) Does the program run after you change the default file system to HDFS? What is the error message, if any, that you get?
  The program did not run. Error message i got was:
  Input file 'AREAWATER_madhi002.csv' does not exist!

* (Q8) Use your program to test the following cases and report the running time for each case.
  i. Copy a file from local file system to HDFS
  Copied 2271210910 bytes from 'file:/home/cs167/AREAWATER_madhi002.csv' to 'hdfs://class-130:9000/output2.csv' in 20.141520 seconds
  ii. Copy a file from HDFS to local file system.
  Copied 2271210910 bytes from 'hdfs://class-130:9000/output2.csv' to 'file:/home/cs167/AREAWATER_ii.csv' in 19.824696 seconds
  iii. Copy a file from HDFS to HDFS.
  Copied 2271210910 bytes from 'hdfs://class-130:9000/output2.csv' to 'hdfs://class-130:9000/AREAWATER_iii.csv' in 20.934350 seconds
  iv. Table for times:
  Colons can be used to align columns.

| File Systems  | Time(seconds) |
|:-------------:|:-------------:|
| Local to HDFS |   20.141520   |
| HDFS to Local |   19.824696   |
| HDFS to HDFS  |   20.934350   |
