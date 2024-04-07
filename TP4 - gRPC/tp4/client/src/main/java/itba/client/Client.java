package itba.client;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import itba.tp2_service.Tp2ServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("tp4 Client Starting ...");
        logger.info("grpc-com-patterns Client Starting ...");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        try {
            // Create stub for Tp2Service
            Tp2ServiceGrpc.Tp2ServiceBlockingStub stub = Tp2ServiceGrpc.newBlockingStub(channel);

            // Call ping
            StringValue pong = stub.ping(Empty.getDefaultInstance());
            logger.info("Received: " + pong.getValue());

            // Call time
            StringValue time = stub.time(Empty.getDefaultInstance());
            logger.info("Received: " + time.getValue());

            // Call echo
            StringValue echo = stub.echo(StringValue.of("Hello world"));
            logger.info("Received: " + echo.getValue());

            // Call hello
            StringValue hello = stub.hello(StringValue.of("Charly"));
            logger.info("Received: " + hello.getValue());

            // Call fortune
            StringValue fortune = stub.fortune(Empty.getDefaultInstance());
            logger.info("Received: " + fortune.getValue());
        } finally {
            channel.shutdown().awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}
