package com.example;

import io.grpc.stub.StreamObserver;

import java.util.Set;

public class OrderService extends OrderServiceGrpc.OrderServiceImplBase {

    @Override
    public void getOrdersForUser(OrdersForUserRequest request, StreamObserver<OrdersForUserResponse> responseObserver) {
        int userId = request.getUserId();

        Set<Order> userOrders = getDummyOrdersForUser(userId);

        OrdersForUserResponse response = OrdersForUserResponse.newBuilder()
                .addAllOrder(userOrders)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private Set<Order> getDummyOrdersForUser(int userId) {
        Order order1 = Order.newBuilder()
                .setUserId(userId)
                .setOrderId(1)
                .setNumberOfItems(10)
                .setAmount(10.3)
                .build();

        Order order2 = Order.newBuilder()
                .setUserId(userId)
                .setOrderId(2)
                .setNumberOfItems(2)
                .setAmount(23)
                .build();

        return Set.of(order1, order2);

    }
}
