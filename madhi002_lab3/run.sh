mvn package
hadoop jar target/madhi002_lab3-1.0-SNAPSHOT.jar edu.ucr.cs.cs167.madhi002.App file:///'pwd'/AREAWATER_madhi002.csv hdfs:///class-130:9000/AreaSubmission.csv
hadoop jar target/madhi002_lab3-1.0-SNAPSHOT.jar edu.ucr.cs.cs167.madhi002.App hdfs:///class-130:9000/AreaSubmission.csv file:///'pwd'/outputSub.csv
hadoop jar target/madhi002_lab3-1.0-SNAPSHOT.jar edu.ucr.cs.cs167.madhi002.App hdfs:///class-130:9000/AreaSubmission.csv hdfs:///class-130:900/Areah2.csv



