package ej5;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        DirectoryLineCounter dln = new DirectoryLineCounter();
        try {
            long linesInDirectory = dln.countLinesInDirectory(".");
            System.out.println("The directory has " + linesInDirectory + " lines in total.");
        } catch (IOException e){
            System.out.println("There was an exception: " + e);
        }
    }
}
