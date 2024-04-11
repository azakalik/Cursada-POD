package ar.edu.itba.pod.grpc.server;

import ar.edu.itba.pod.grpc.server.ticket.repository.TicketRepository;
import ar.edu.itba.pod.grpc.trainTickets.*;
import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class TrainTicketService extends TrainTicketServiceGrpc.TrainTicketServiceImplBase {
    private final TicketRepository ticketRepository;
    public TrainTicketService(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }
    @Override
    public void getDestinations(Empty request, StreamObserver<Destinations> responseObserver) {
        Destinations response = Destinations.newBuilder()
                .addAllDestinations(this.ticketRepository.getDestinations())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTrainsForDestination(StringValue request, StreamObserver<Train> responseObserver) {
        this.ticketRepository
            .getAvailability(request.getValue())
            .forEach(train -> responseObserver.onNext(
                Train.newBuilder()
                        .setId(train.id())
                        .setDestination(train.destination())
                        .setTime(train.time())
                        .setAvailableCount(train.availableSeats())
                        .build()
            ));
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Ticket> purchaseTicket(StreamObserver<Reservation> responseObserver) {
        return new StreamObserver<>() {
            private final List<Ticket> tickets = new ArrayList<>();

            @Override
            public void onNext(Ticket ticket) {
                tickets.add(ticket);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(
                        Reservation.newBuilder()
                            .setId(ticketRepository.addReservation(this.tickets))
                            .setTicketCount(this.tickets.size())
                            .build()
                );
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<Reservation> getTicketsForReservations(StreamObserver<Ticket> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(Reservation reservation) {
                ticketRepository.getReservation(reservation.getId()).ifPresent(tickets -> tickets.forEach(responseObserver::onNext));
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}