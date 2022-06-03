package com.example.client;

import com.example.AverageRequest;
import com.example.AverageResponse;
import com.example.CalcServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AverageClient {

  private static void calcAverage(ManagedChannel managedChannel) throws InterruptedException {
    CalcServiceGrpc.CalcServiceStub calcServiceStub = CalcServiceGrpc.newStub(managedChannel);
    CountDownLatch latch = new CountDownLatch(1);

    StreamObserver<AverageRequest> stream = calcServiceStub.average(
        new StreamObserver<>() {
          @Override
          public void onNext(AverageResponse averageResponse) {
            System.out.println(averageResponse.getAverage());
          }

          @Override
          public void onError(Throwable throwable) {
          }

          @Override
          public void onCompleted() {
            latch.countDown();
          }
        });

    Arrays.asList(1, 2, 3, 4)
        .forEach(number -> stream.onNext(AverageRequest.newBuilder().setNumber(number).build()));
    stream.onCompleted();

    latch.await(3, TimeUnit.SECONDS);
  }

  public static void main(String[] args) throws InterruptedException {

    ManagedChannel channel = ManagedChannelBuilder
        .forAddress("localhost", 50010)
        .usePlaintext()
        .build();

    calcAverage(channel);

    channel.shutdown();
  }

}
