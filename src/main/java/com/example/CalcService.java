package com.example;

import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

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

  @Override
  public void prime(PrimeRequest request, StreamObserver<PrimeResponse> responseObserver) {
    List<Integer> primes = getPrimes(request.getNumber());

    primes.forEach(
        prime -> responseObserver.onNext(PrimeResponse.newBuilder().setPrime(prime).build()));

    responseObserver.onCompleted();
  }

  private List<Integer> getPrimes(int number) {
    List<Integer> primes = new ArrayList<>();

    int k = 2;
    while (number > 1) {
      if (number % k == 0) {
        primes.add(k);
        number = number / k;
      } else {
        k++;
      }
    }

    return primes;
  }
}
