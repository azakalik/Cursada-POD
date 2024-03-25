package ej2;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        DirectoryLineCounter dln = new DirectoryLineCounter();
        try {
            dln.printFilesInformation(".");
        } catch (IOException e){
            System.out.println("There was an exception: " + e);
        }
    }
}
