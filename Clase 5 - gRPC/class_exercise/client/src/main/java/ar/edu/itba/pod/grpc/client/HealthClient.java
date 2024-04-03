package ar.edu.itba.pod.grpc.client;

import ar.edu.itba.pod.grpc.health.HealthServiceGrpc;
import ar.edu.itba.pod.grpc.health.PingRequest;
import ar.edu.itba.pod.grpc.health.PingResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class HealthClient {
    private static Logger logger = LoggerFactory.getLogger(HealthClient.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("class_exercise Client Starting ...");
        logger.info("grpc-com-patterns Client Starting ...");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        try {
            HealthServiceGrpc.HealthServiceBlockingStub stub = HealthServiceGrpc.newBlockingStub(channel);
            PingRequest request = PingRequest.getDefaultInstance();
            PingResponse repsonse =  stub.ping(request);
        } finally {
            channel.shutdown().awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}
