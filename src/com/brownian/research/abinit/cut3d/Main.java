package com.brownian.research.abinit.cut3d;

import java.io.*;
import java.util.IllegalFormatException;
import java.util.Scanner;

public class Main {

    static final String PROGRAM_NAME = "java -jar spacedToCSV";
    private static boolean isVerbose = false; //if true, can print verbose messages to stderr

    public static void main(String[] args) {

        String inputFilePath = null, outputFilePath = null;

        //parse command-line arguments
        for(int i = 0 ; i < args.length ; i++){
            if(args[i].equals("-in")){
                if(i+1 >= args.length){
                    printUserErrorMessageAndQuit("Input file not specified.");
                }
                else if(inputFilePath != null){
                    printUserErrorMessageAndQuit("Input file already specified");
                } else {
                    i++; //increment the counter to skip over -in and the filename
                    inputFilePath = args[i];
                }
            } else if(args[i].equals("-out")){
                if(i+1 >= args.length){
                    printUserErrorMessageAndQuit("Output file not specified.");
                }
                else if(outputFilePath != null){
                    printUserErrorMessageAndQuit("Output file already specified");
                } else {
                    i++; //increment the counter to skip over -in and the filename
                    outputFilePath = args[i];
                }
            } else if(isVerboseFlag(args[i])) {
                isVerbose = true;
            } else if(isHelpFlag(args[i])){
                printHelpMessageTo(System.err);
                return;
            } else if(inputFilePath == null){
                inputFilePath = args[i];
            } else if(outputFilePath == null){
                outputFilePath = args[i];
            } else {
                printUserErrorMessageAndQuit("Unexpected parameter: "+args[0]);
            }
        }

        InputStreamReader inputStreamReader;
        OutputStreamWriter outputStreamWriter;

        try {
            inputStreamReader = getInputStreamReaderFrom(inputFilePath);
        } catch(FileNotFoundException e){
            System.err.println("Could not open file "+inputFilePath);
            verboseLog(e);
            return; //explicitly quit the program
        }
        try {
            outputStreamWriter = getOutputStreamWriterFrom(outputFilePath);
        } catch(FileNotFoundException e){
            System.err.println("Could not open file "+outputFilePath);
            verboseLog(e);
            return; //explicitly quit the program
        } catch (IOException e){
            System.err.println("Error opening "+outputFilePath+" to write.");
            verboseLog(e);
            return; //explicitly quit the program
        }

        executeConversion(inputStreamReader, outputStreamWriter);
    }


    private static void executeConversion(InputStreamReader inputStreamReader, OutputStreamWriter outputStreamWriter) throws IllegalFormatException{
        Scanner convenientLineReader = new Scanner(inputStreamReader);
        PrintWriter convenientLineWriter = new PrintWriter(new BufferedWriter(outputStreamWriter));

        verbosePrintln("Beginning conversion...");

        while(convenientLineReader.hasNextLine()){
            convenientLineWriter.println(convenientLineReader.nextLine().trim().replaceAll(" +",","));
        }

        verbosePrintln("Done.");
    }

    /**
     * Gets an {@link OutputStreamWriter} to write to.
     * If null is given, this uses stdout.
     * @param outputFilePath the (optional) file path to write to
     * @return an {@link OutputStreamWriter} to write to
     * @throws FileNotFoundException if the path specified is not a valid file
     * @throws IOException if an I/O problem arises while establishing the output stream
     */
    private static OutputStreamWriter getOutputStreamWriterFrom(String outputFilePath) throws FileNotFoundException, IOException {
        if(outputFilePath == null){
            verbosePrintln("No output file specified; outputting to stdout");
            return new OutputStreamWriter(System.out);
        }
        File outputFile = new File(outputFilePath);
        return new FileWriter(outputFile);
    }

    /**
     * Gets an {@link InputStreamReader} to read input from.
     * If null is given, then uses stdin.
     * @param inputFilePath the (optional) file path to read from
     * @return an {@link InputStreamReader} to read from
     * @throws FileNotFoundException if the path specified is not a valid file
     */
    private static InputStreamReader getInputStreamReaderFrom(String inputFilePath) throws FileNotFoundException{
        if(inputFilePath == null){
            verbosePrintln("No input file specified; outputting to stdin");
            return new InputStreamReader(System.in);
        }
        File inputFile = new File(inputFilePath);
        return new FileReader(inputFile);
    }

    /**
     * Prints a message to stderr, but ONLY if verbose logging is enabled.
     * @param message the message to print
     */
    private static void verbosePrintln(String message){
        if(isVerbose)
            System.err.println(message);
    }

    private static void verboseLog(Exception e){
        if(isVerbose)
            e.printStackTrace(System.err);
    }

    private static boolean isHelpFlag(String flag){
        return flag.equals("-h") || flag.equals("--help") || flag.equals("/?");
    }

    private static boolean isVerboseFlag(String flag){
        return flag.equals("-v") || flag.equals("--verbose");
    }

    private static void printHelpMessageTo(PrintStream stream){
        stream.println("Custom csv adapter for 3d-indexed files generated by Abinit Cut3d (option 6).");
        stream.println("Specifically, it reads lines of values delineated by spaces, and outputs lines of values delineated by commas.");
        stream.println("If no input/output files are specified, then uses stdin and stdout, respectively.");
        printUsageInformationTo(stream);
    }

    private static void printUsageInformationTo(PrintStream stream){
        stream.println("Usage:");
        stream.println("\t"+PROGRAM_NAME+" [-v|--verbose] [-in] <input file>] [[-out] <output file>]\n\t\tConvert a file and output the result to stdout or the specified file");
        stream.println("\t"+PROGRAM_NAME+" [-h]\n\t\tDisplay this help message");
    }

    static void printUserErrorMessage(String message){
        System.err.println(message);
        printUsageInformationTo(System.err);
    }

    private static void printUserErrorMessageAndQuit(String message){
        printUserErrorMessage(message);
        System.exit(1);
    }
}
