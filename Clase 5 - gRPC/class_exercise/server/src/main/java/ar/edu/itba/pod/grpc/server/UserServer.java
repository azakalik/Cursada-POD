package ar.edu.itba.pod.grpc.server;

import ar.edu.itba.pod.grpc.health.HealthServiceGrpc.HealthServiceImplBase;
import ar.edu.itba.pod.grpc.health.PingRequest;
import ar.edu.itba.pod.grpc.health.PingResponse;
import ar.edu.itba.pod.grpc.user.*;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UserServer {
    private static Logger logger = LoggerFactory.getLogger(UserServer.class);


    //USAR EXTENDS
    public static class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
        @Override
        public void doLogin(LoginInformation request, StreamObserver<User> responseObserver){
            User response = User.newBuilder()
                    .setUserName(request.getUserName())
                    .setDisplayName("Foo")
                    .addPreferences("darkMode")
                    .setStatus(AccountStatus.ACCOUNT_STATUS_ACTIVE)
                    .build()
                    ;
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void getRoles(User request, StreamObserver<UserRoles> responseObserver){
            UserRoles userRoles = UserRoles.newBuilder()
                    .putRolesBySite("Home", Role.BUYER)
                    .build()
                    ;
            responseObserver.onNext(userRoles);
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        logger.info(" Server Starting ...");

        int port = 50051;
        io.grpc.Server server = ServerBuilder.forPort(port).addService(new UserServiceImpl())
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