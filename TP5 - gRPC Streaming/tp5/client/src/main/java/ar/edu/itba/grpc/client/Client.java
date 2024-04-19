package ar.edu.itba.grpc.client;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import library.BookService;
import library.LibraryServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("tp5 Client Starting ...");
        logger.info("grpc-com-patterns Client Starting ...");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        try {
            // Create blocking stub
            LibraryServiceGrpc.LibraryServiceBlockingStub blockingStub = LibraryServiceGrpc.newBlockingStub(channel);

            // Test listBooks
            BookService.ListBooksResponse listBooksResponse = blockingStub.listBooks(Empty.getDefaultInstance());
            List<String> availableBookIsbns = listBooksResponse.getValuesList().stream().map(infoString -> infoString.split(" - ")[0]).toList();
            logger.info("Available books: " + availableBookIsbns);

            // Test lendBook
            BookService.LendBookResponse lendBookResponse;
            // Nonexistent ISBN
            try {
                lendBookResponse = blockingStub.lendBook(StringValue.of("1"));
            } catch(Exception e) {
                logger.error("Nonexistent ISBN");
            }
            // Available books
            List<BookService.LendBookResponse> lentBooks = availableBookIsbns.stream().map(isbn -> blockingStub.lendBook(StringValue.of(isbn))).toList();
            logger.info("Lent books: " + lentBooks.stream().map(BookService.LendBookResponse::getIsbn));
            // Already lent book
            try {
                lendBookResponse = blockingStub.lendBook(StringValue.of("978-0307743657"));
            } catch (Exception e) {
                logger.error("Already lent book");
            }

            // Test returnBook
            lentBooks.forEach(blockingStub::returnBook);
        } finally {
            channel.shutdown().awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}
