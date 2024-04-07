package itba.server;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import itba.tp2_service.Tp2ServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Server {
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    private static final String[] fortunes = {
        "Good things are being attracted to you.",
        "You will be surrounded by things of luxury.",
        "A dream you have will come true.",
        "Your hard work will soon pay off.",
        "You will become great if you believe in yourself."
    };
    private static final Random random = new Random();

    public static class Tp2ServiceImpl extends Tp2ServiceGrpc.Tp2ServiceImplBase {

        @Override
        public void ping(Empty request, StreamObserver<StringValue> responseObserver) {
            responseObserver.onNext(StringValue.of("Pong"));
            responseObserver.onCompleted();
        }

        @Override
        public void time(Empty request, StreamObserver<StringValue> responseObserver) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String datetime = now.format(formatter);
            responseObserver.onNext(StringValue.of(datetime));
            responseObserver.onCompleted();
        }

        @Override
        public void echo(StringValue request, StreamObserver<StringValue> responseObserver) {
            String received = request.getValue();
            responseObserver.onNext(StringValue.of(received));
            responseObserver.onCompleted();
        }

        @Override
        public void hello(StringValue request, StreamObserver<StringValue> responseObserver) {
            String received = request.getValue();
            responseObserver.onNext(StringValue.of("Hello " + received));
            responseObserver.onCompleted();
        }

        @Override
        public void fortune(Empty request, StreamObserver<StringValue> responseObserver) {
            int index = random.nextInt(fortunes.length);
            responseObserver.onNext(StringValue.of(fortunes[index]));
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        logger.info(" Server Starting ...");

        int port = 50051;
        io.grpc.Server server = ServerBuilder
                .forPort(port)
                .addService(new Tp2ServiceImpl())
                .build();
        server.start();
        logger.info("Server started, listening on " + port);
        server.awaitTermination();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down gRPC server since JVM is shutting down");
            server.shutdown();
            logger.info("Server shut down");
        }));
    }
}
