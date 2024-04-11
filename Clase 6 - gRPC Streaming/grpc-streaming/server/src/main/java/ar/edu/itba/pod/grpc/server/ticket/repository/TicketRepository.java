package ar.edu.itba.pod.grpc.server.ticket.repository;



import ar.edu.itba.pod.grpc.trainTickets.Ticket;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketRepository {
    private static final String[] destinations = {"Mar del Plata", "Rosario", "Cordoba", "Tucuman"};
    private static final Random random = new Random();

    private final Map<String, List<ar.edu.itba.pod.grpc.server.ticket.repository.Train>> trainsByDestination;

    private final Map<String, List<Ticket>> reservations;

    public TicketRepository() {
        trainsByDestination = new HashMap<>();
        reservations = new HashMap<>();

        for (int i = 0; i < destinations.length; i++) {
            final String destination = destinations[i];
            final List<ar.edu.itba.pod.grpc.server.ticket.repository.Train> trainList = IntStream.range(1, random.nextInt(1, 5)).mapToObj(index ->
                    new ar.edu.itba.pod.grpc.server.ticket.repository.Train(UUID.randomUUID().toString(), destination, String.valueOf(index), random.nextInt(10))
            ).collect(Collectors.toList());

            trainsByDestination.put(destination, trainList);
        }
    }

    public List<String> getDestinations() {
        return List.of(destinations);
    }

    public List<ar.edu.itba.pod.grpc.server.ticket.repository.Train> getAvailability(final String destination) {
        return trainsByDestination.get(destination);
    }

    public String addReservation(List<Ticket> tickets) {
        final String reservationId = UUID.randomUUID().toString();
        reservations.put(reservationId, tickets);

        return reservationId;
    }

    public Optional<List<Ticket>> getReservation(final String id) {
        return Optional.ofNullable(reservations.get(id));
    }

}

