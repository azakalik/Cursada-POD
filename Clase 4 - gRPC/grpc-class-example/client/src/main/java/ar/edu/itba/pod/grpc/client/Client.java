package ar.edu.itba.pod.grpc.client;

import ar.edu.itba.pod.grpc.GreeterGrpc;
import ar.edu.itba.pod.grpc.HelloReply;
import ar.edu.itba.pod.grpc.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Client {
	private static Logger logger = LoggerFactory.getLogger(Client.class);

	public static void main(String[] args) throws InterruptedException {
		logger.info("grpc-class-example Client Starting ...");
		String user = "pod";
		// Access a service running on the local machine on port 50051
		String target = "localhost:50051";

		ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
				// Channels are secure by default (via SSL/TLS). For the example we disable TLS
				// to avoid
				// needing certificates.
				.usePlaintext().build();
		logger.info("grpc-class-example Client Starting ...");

		try {
			logger.info("grpc-class-example Client Starting ...");
			HelloRequest helloRequest = HelloRequest.newBuilder().setName("carlitos").build();
			GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel);
			HelloReply reply = blockingStub.sayHello(helloRequest);
			System.out.println(reply.getMessage());
		} finally {
			// ManagedChannels use resources like threads and TCP connections. To prevent
			// leaking these
			// resources the channel should be shut down when it will no longer be used. If
			// it may be used
			// again leave it running.
			channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
		}
	}
}
