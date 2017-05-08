# ConvertSpacedDataToCSV
This is a custom tool I created to help me in my research.

It converts the 3D-indexed output of [Abinit](http://www.abinit.org/)'s [Cut3D](http://www.abinit.org/doc/helpfiles/for-v8.2/users/cut3d_help.html) utility to CSV format.
More generally, it takes lines with data separated by consecutive spaces, and outputs lines with data separated by single commas.

# Features

* Converts space-delimited input to comma-delimited output, ignoring spaces at the start/end of a line.
* Accepts input through stdin or from a specified file
* Output through stdout or to a specified file
* Only recognizes the space character, not tabs or other whitespace.

# Using

These examples assume that the program is compiled into `spacedToCSV.jar`.

    java -jar spacedToCSV.jar (-h|--help|/?|/h)                                           Prints a help message
    java -jar spacedToCSV.jar [-v|--verbose] [[-in] <input file>] [[-out] <output file>]  Reads from stdin or <input file> and writes to stdout or <output file>

## Flags

* `-in` Specifies an input file to read from.
* `-out` Specifies an output file to write to.
* `-v` `--verbose` Turns on detailed logging to stderr
* `-h` `--help` `/?` `/h` Displays a help message.

## Example usage

Read from `sample_DEX_file` and write to `output.csv`:

    java -jar spacedToCSV.jar sample_DEX_file output.csv
    
Read from stdin and write to stdout:

    cat sample_DEX_file | java -jar spacedToCSV.jar
    
Read from `sample_DEX_file` and write to `output.csv`:

    java -jar spacedToCSV.jar -out output.csv -in sample_DEX_file
    
Gather more details about errors:

    java -jar spacedToCSV.jar --verbose badFile.dat

# Dependencies

This program depends on Java. I wrote it using Java 8, but it should be supported on Java 6 or earlier.

# Building

The only file is [Main.java](src/com/brownian/research/abinit/cut3d/Main.java), which is also the "main" class.

## Using IntelliJ

Add an artifact to your project structure that will compile the output of building into a .jar of your choosing.

## Command Line

Please [read this article](https://docs.oracle.com/javase/tutorial/deployment/jar/build.html) for instructions for creating a .jar using the command line.
The following two lines should work:

    javac ./src/com/brownian/research/abinit/cut3d/Main.java
    jar cfm ./src/META_INF/MANIFEST.MF spacedToCSV.jar /path/to/Main.class
    
# Contributing

This was created to be a tool to help me with my personal research, but it you have a suggestion, I'd love to hear it.

Please give suggestions by opening an Issue, or submit code via a Pull Request.

# License

[This code is licensed under the MIT License](LICENSE.md). Feel free to fork it, modify it, or whatever else you want, as outlined more explicitly in the aforementioned license.
