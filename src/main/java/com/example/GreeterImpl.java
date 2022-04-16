package com.example;

import io.grpc.stub.StreamObserver;

public class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        HelloResponse helloResponse = HelloResponse.newBuilder()
                .setMessage("hello " + request.getName())
                .build();

        responseObserver.onNext(helloResponse);
        responseObserver.onCompleted();
    }
}
