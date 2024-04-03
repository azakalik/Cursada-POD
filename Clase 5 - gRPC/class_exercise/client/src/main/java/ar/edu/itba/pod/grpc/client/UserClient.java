package ar.edu.itba.pod.grpc.client;

import ar.edu.itba.pod.grpc.health.HealthServiceGrpc;
import ar.edu.itba.pod.grpc.health.PingRequest;
import ar.edu.itba.pod.grpc.health.PingResponse;
import ar.edu.itba.pod.grpc.user.LoginInformation;
import ar.edu.itba.pod.grpc.user.User;
import ar.edu.itba.pod.grpc.user.UserServiceGrpc;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UserClient {
    private static Logger logger = LoggerFactory.getLogger(UserClient.class);

    static class UserCallback implements FutureCallback<User>{
        private static Logger logger = LoggerFactory.getLogger(UserCallback.class);

        @Override
        public void onSuccess(User user) {
            logger.info("received user {}", user);
        }

        @Override
        public void onFailure(Throwable throwable) {
            logger.error("reaching for user", throwable);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        logger.info("class_exercise Client Starting ...");
        logger.info("grpc-com-patterns Client Starting ...");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        UserCallback userCallback = new UserCallback();

        try {
//            UserServiceGrpc.UserServiceBlockingStub stub = UserServiceGrpc.newBlockingStub(channel);
            UserServiceGrpc.UserServiceFutureStub stub = UserServiceGrpc.newFutureStub(channel);
            LoginInformation loginInformation = LoginInformation
                    .newBuilder()
                    .setUserName("username")
                    .setPassword("password")
                    .build()
                    ;
            ListenableFuture<User> userListenableFuture = stub.doLogin(loginInformation);
            Futures.addCallback(userListenableFuture, userCallback, executorService);
            User user = userListenableFuture.get();
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            channel.shutdown().awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}
