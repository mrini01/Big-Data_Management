package edu.ucr.cs.cs167.madhi002;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
        if(args.length != 2){
            System.err.println("Incorrect number of arguments! Expected two arguments.");
            System.exit(-1);
        }

        Configuration config = new Configuration();
        Path inputFile = new Path(args[0]);
        Path outputFile = new Path(args[1]);

        FileSystem inFile = inputFile.getFileSystem((config));
        FileSystem outFile = outputFile.getFileSystem((config));

        if(!inFile.exists(inputFile)){
            System.err.printf("Input file '%s' does not exist!\n", inputFile);
            System.exit(-1);
        }

        if(outFile.exists(outputFile)){
            System.err.printf("Output file '%s' already exists!\n", outputFile);
            System.exit(-1);
        }


        FSDataInputStream inputStream = inFile.open(inputFile);
        FSDataOutputStream outputStream = outFile.create(outputFile);

        long startTime = System.nanoTime();

        byte[] buffer = new byte[4 * 1024 * 1024];
        int bytesRead = 0;
        long bytesCopied = 0;

        while((bytesRead = inputStream.read(buffer)) > 0 ){
            outputStream.write(buffer, 0, bytesRead);
            bytesCopied += bytesRead;
        }

        inputStream.close();
        outputStream.close();

        long endTime = System.nanoTime();

        System.out.printf("Copied %d bytes from '%s' to '%s' in %f seconds\n",
                bytesCopied, inputFile, outputFile, (endTime - startTime) * 1E-9);

    }
}
