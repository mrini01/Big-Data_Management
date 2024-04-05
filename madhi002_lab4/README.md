# Lab 4

## Student information

* Full name: Mrinisha Adhikari
* E-mail: madhi002@ucr.edu
* UCR NetID: madhi002
* Student ID: 862309931

## Answers

* (Q1) What do you think the line `job.setJarByClass(Filter.class);` does?
I think that the line will use the specified JAR file from the filter class to do the MapReduce job. Basically informing hadoop of the code that needs to go across the different nodes in a given cluster.

* (Q2) What is the effect of the line `job.setNumReduceTasks(0);`?
Since it sets the number of reduce tasks to 0, there won't be a reduce phase after the map phase, so the final output will be map phase. There also won't be any need to aggregate as the reduce phase does not occur. 

* (Q3) Where does the `main` function run? (Driver node, Master node, or an executor node).
The 'main' function will run in the driver node.

* (Q4) How many lines do you see in the output?
27972 lines of code (file: part-m-00000)

* (Q5) How many files are produced in the output?
For nasa_1995080.tsv - 1 file: part-m-00000, the rest are SUCCESS and crc files, with total being 4 files. 
For nasa_19950630.22-19950728.12.tsv - 5 files: part-m-00000,part-m-00001,part-m-00002,part-m-00003,part-m-00004, the rest are SUCCESS and crc files, with total being 12 files. 

* (Q6) Explain this number based on the input file size and default block size.
Here, since we don't have a reducer phase, the mapper tasks will depend upon input splits. Since the local file system uses 32MB, for nasa_1995080.tsv, its size is 2.2 MiB, so its 
less than 32MB, hence the need for only 1 output file. The other file nasa_19950630.22-19950728.12.tsv is 141.6 MiB, which is 148.5MB. Meaning a total of 5 output files from the mapper is
needed: 32MB×5 = 160MB, which fits the needed 141.6 MiB, and 5 output files are used. 

* (Q7) How many files are produced in the output directory and how many lines are there in each file?
2 of the specific files: part-r-00000 has 4 lines, and part-r-00001 has 0 lines. The rest are SUCCESS and crc files, with total being 6 files. 

* (Q8) Explain these numbers based on the number of reducers and number of response codes in the input file.
As we are using 2 reducers in aggregation, we have two output files. Since only part-r-00000 has lines in it, only one file is needed to write nasa_19950801_madhi002.tsv. For the
second file, part-r-00001, it has no lines because all the code reducers was just needed for the first file. Example, the 1st line in part-r-00000: 200	481974462. 

* (Q9) How many files are produced in the output directory and how many lines are there in each file?
2 of the specific files: part-r-00000 has 5 lines, and part-r-00001 has 2 lines. The rest are SUCCESS and crc files, with total being 6 files. 

* (Q10) Explain these numbers based on the number of reducers and number of response codes in the input file.
Once again using the aggregation file means two output files. In this case, the output code lines, has the response codes in both files as it's a larger file.  For each file we see
a response code and then the total bytes for the given response code. Example, the 1st line in part-r-00000: 200 37585778. 

* (Q11) How many files are produced in the output directory and how many lines are there in each file?
2 of the specific files: part-r-00000 has 1 line, and part-r-00001 has 0 lines. The rest are SUCCESS and crc files, with total being 6 files. 

* (Q12) Explain these numbers based on the number of reducers and number of response codes in the input file.
Since we use filter_output as the input file for aggregation, it has already been filtered for 200 response code through the filter class. Then, putting that through the aggregation
class, we have reducers to two output files, where only part-r-00000 has 1 line of code as its already been passed through the filter.
