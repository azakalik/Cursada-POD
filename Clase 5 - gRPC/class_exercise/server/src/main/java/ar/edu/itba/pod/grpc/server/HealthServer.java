package ar.edu.itba.pod.grpc.server;

import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ar.edu.itba.pod.grpc.health.HealthServiceGrpc.HealthServiceImplBase;
import ar.edu.itba.pod.grpc.health.PingRequest;
import ar.edu.itba.pod.grpc.health.PingResponse;

import java.io.IOException;

public class HealthServer {
    private static Logger logger = LoggerFactory.getLogger(HealthServer.class);


    //USAR EXTENDS
    public static class HealthServiceImpl extends HealthServiceImplBase {

        @Override
        public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver){
            PingResponse pingResponse = PingResponse.newBuilder().setMessage("Hola Mundo!").build();


            responseObserver.onNext(pingResponse);
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        logger.info(" Server Starting ...");

        int port = 50051;
        io.grpc.Server server = ServerBuilder.forPort(port).addService(new HealthServiceImpl())
                .build();
        server.start();
        logger.info("Server started, listening on " + port);
        server.awaitTermination();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down gRPC server since JVM is shutting down");
            server.shutdown();
            logger.info("Server shut down");
        }));
    }}