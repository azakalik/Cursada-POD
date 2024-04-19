package ar.edu.itba.grpc.server.libraryservice;

import ar.edu.itba.grpc.server.Server;
import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import io.grpc.stub.StreamObserver;
import library.BookService;
import library.LibraryServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class LibraryService extends LibraryServiceGrpc.LibraryServiceImplBase {
    private static Logger logger = LoggerFactory.getLogger(LibraryService.class);
    private final HashMap<String, Book> bookHashMap = new HashMap<>();
    public LibraryService(Collection<String[]> books){
        books.forEach(book -> bookHashMap.put(
                book[1],
                new Book(Integer.parseInt(book[0]), book[1], book[2], book[3], book[4], book[5]))
        );
    }
    @Override
    public void listBooks(Empty request, StreamObserver<BookService.ListBooksResponse> responseObserver) {
        logger.info("Listing books");
        List<String> values = new ArrayList<>();
        bookHashMap.values().forEach(book -> values.add(book.getIsbn() + " - " + book.getTitle()));
        responseObserver.onNext(BookService.ListBooksResponse.newBuilder().addAllValues(values).build());
        responseObserver.onCompleted();
    }

    @Override
    public void lendBook(StringValue request, StreamObserver<BookService.LendBookResponse> responseObserver) {
        String isbn = request.getValue();
        if (!bookHashMap.containsKey(isbn)){
            logger.info("Nonexistent ISBN: " + isbn);
            responseObserver.onError(new NonExistentIsbnException(isbn));
            return;
        }
        Book book = bookHashMap.get(isbn);
        Boolean lentBook = book.lendBook();
        if (!lentBook){
            logger.info("Book not available: " + isbn);
            responseObserver.onError(new BookNotAvailableException(isbn));
            return;
        }
        logger.info("Book lent: " + isbn);
        responseObserver.onNext(BookService.LendBookResponse.newBuilder()
                .setIsbn(isbn)
                .setTitle(book.getTitle())
                .setDate(book.getReleaseDate())
                .setAuthor(BookService.LendBookResponse.AuthorResponse.newBuilder()
                        .setFirstName(book.getAuthorFirstName())
                        .setLastName(book.getAuthorLastName())
                        .build())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void returnBook(BookService.LendBookResponse request, StreamObserver<Empty> responseObserver) {
        String isbn = request.getIsbn();
        if (!bookHashMap.containsKey(isbn)){
            logger.info("Nonexistent ISBN: " + isbn);
            responseObserver.onError(new NonExistentIsbnException(isbn));
            return;
        }
        Book book = bookHashMap.get(isbn);
        book.returnBook();
        logger.info("Book returned: " + isbn);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
