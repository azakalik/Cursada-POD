package ar.edu.itba.pod.grpc.server;

import ar.edu.itba.pod.grpc.HelloReply;
import ar.edu.itba.pod.grpc.HelloRequest;
import ar.edu.itba.pod.grpc.GreeterGrpc.GreeterImplBase;
import io.grpc.stub.StreamObserver;

public class GreeterServant extends GreeterImplBase {
    @Override
     public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
         HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
         responseObserver.onNext(reply);
         responseObserver.onCompleted();
     }
}
