package ar.edu.itba.grpc.server.libraryservice;

public class BookNotAvailableException extends Exception{
    public BookNotAvailableException(String isbn) {
        super("All instances of ISBN " + isbn + " are already lent");
    }
}
