package com.example;

import com.example.client.OrderClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService extends UserServiceGrpc.UserServiceImplBase {

    private final Logger logger = Logger.getLogger(UserService.class.getName());

    @Override
    public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        int userId = request.getUserId();

        List<Order> orders = getOrders(userId);

        double totalAmount = orders.stream()
                .map(Order::getAmount)
                .reduce(Double::sum)
                .orElse(0d);

        UserResponse response = UserResponse.newBuilder()
                .setName("John")
                .setUserId(userId)
                .setNumberOfOrders(orders.size())
                .setTotalAmount(totalAmount)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private List<Order> getOrders(int userId) {
        // get user orders from Order service
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:50006")
                .usePlaintext()
                .build();

        OrderClient orderClient = new OrderClient(channel);

        List<Order> orders = orderClient.getOrders(userId);

        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Can not close the channel", e);
        }
        return orders;
    }
}
