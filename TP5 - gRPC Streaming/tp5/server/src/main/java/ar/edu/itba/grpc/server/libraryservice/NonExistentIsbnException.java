package ar.edu.itba.grpc.server.libraryservice;

public class NonExistentIsbnException extends Exception{
    public NonExistentIsbnException(String isbn) {
        super("ISBN " + isbn + " does not exist");
    }
}
