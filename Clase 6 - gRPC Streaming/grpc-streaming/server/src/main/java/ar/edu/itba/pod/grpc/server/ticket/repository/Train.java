package ar.edu.itba.pod.grpc.server.ticket.repository;

public record Train(
        String id,
        String destination,
        String time,
        int availableSeats
) {
}
