package com.example.client;

import com.example.Order;
import com.example.OrderServiceGrpc;
import com.example.OrdersForUserRequest;
import com.example.OrdersForUserResponse;
import io.grpc.Channel;

import java.util.List;

public class OrderClient {

    private final OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    public OrderClient(Channel channel) {
        this.orderServiceBlockingStub = OrderServiceGrpc.newBlockingStub(channel);
    }

    public List<Order> getOrders(int userId) {
        OrdersForUserRequest request = OrdersForUserRequest.newBuilder()
                .setUserId(userId)
                .build();

        OrdersForUserResponse response = orderServiceBlockingStub.getOrdersForUser(request);

        return response.getOrderList();
    }
}
