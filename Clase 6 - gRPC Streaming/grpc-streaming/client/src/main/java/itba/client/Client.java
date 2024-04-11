package itba.client;

import ar.edu.itba.pod.grpc.trainTickets.*;
import com.google.protobuf.StringValue;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("grpc-streaming Client Starting ...");
        logger.info("grpc-com-patterns Client Starting ...");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        try {
            System.out.println("TEST 1");
            // Test getDestinations method, first we create blocking stub
            TrainTicketServiceGrpc.TrainTicketServiceBlockingStub stub = TrainTicketServiceGrpc.newBlockingStub(channel);
            final Destinations destinations = stub.getDestinations(com.google.protobuf.Empty.getDefaultInstance());
            destinations.getDestinationsList().forEach(System.out::println);

            System.out.println("TEST 2");
            // Test getTrainsFromDestination method
            // The forEach is executed in a blocking way. The results are printed as soon as they are received, and it waits for the next result.
            final Iterator<Train> trains = stub.getTrainsForDestination(StringValue.of(destinations.getDestinations(0)));
            trains.forEachRemaining(System.out::println);

            System.out.println("TEST 3");
            final CountDownLatch finishLatch = new CountDownLatch(1);
            // Test purchaseTicket method, first we open a new async stub
            TrainTicketServiceGrpc.TrainTicketServiceStub asyncStub = TrainTicketServiceGrpc.newStub(channel);
            List<Reservation> reservations = new ArrayList<>(); // This will end up having 1 element
            StreamObserver<Ticket> ticketStreamObserver = asyncStub.purchaseTicket(new StreamObserver<>(){
                @Override
                public void onNext(Reservation reservation) {
                    System.out.println(reservation);
                    reservations.add(reservation);
                }
                @Override
                public void onError(Throwable throwable) {}
                @Override
                public void onCompleted() {
                    finishLatch.countDown();
                }
            });
            List.of(
                    Ticket.newBuilder().setId("1").setTrainId("1").setPassengerName("John").build(),
                    Ticket.newBuilder().setId("2").setTrainId("1").setPassengerName("Jane").build(),
                    Ticket.newBuilder().setId("3").setTrainId("1").setPassengerName("Jack").build(),
                    Ticket.newBuilder().setId("4").setTrainId("1").setPassengerName("Jim").build()
            ).forEach(ticketStreamObserver::onNext);
            ticketStreamObserver.onCompleted();
            finishLatch.await();


            System.out.println("TEST 4");
            final CountDownLatch finishLatch2 = new CountDownLatch(1);
            // Test getTicketsForReservations method, we use the existing async stub and the reservations list from previous test
            StreamObserver<Reservation> reservationStreamObserver = asyncStub.getTicketsForReservations(new StreamObserver<>(){
                @Override
                public void onNext(Ticket ticket) {
                    System.out.println(ticket);
                }
                @Override
                public void onError(Throwable throwable) {}
                @Override
                public void onCompleted() {
                    finishLatch2.countDown();
                }
            });
            reservations.forEach(reservationStreamObserver::onNext);
            reservationStreamObserver.onCompleted();

        } finally {
            channel.shutdown().awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}
