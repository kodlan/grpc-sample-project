package com.example;

import io.grpc.stub.StreamObserver;

public class CalcService extends CalcServiceGrpc.CalcServiceImplBase {

    @Override
    public void calc(CalcRequest request, StreamObserver<CalcResponse> responseObserver) {
        int num1 = request.getNum1();
        int num2 = request.getNum2();
        CalcRequest.Operation op = request.getOperation();

        int res = switch (op) {
            case SUM -> num1 + num2;
            case SUB -> num1 - num2;
            case MUL -> num1 * num2;
            case DIV -> num1 / num2;
            default -> 0;
        };

        CalcResponse response = CalcResponse.newBuilder()
                .setResult(res)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
