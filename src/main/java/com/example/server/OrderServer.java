package com.example.server;

import com.example.OrderService;
import com.example.UserService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderServer {

    private static final Logger logger = Logger.getLogger(UserServer.class.getName());

    private Server server;

    public void startServer() {
        int port = 50006;

        try {
            server = ServerBuilder
                    .forPort(port)
                    .addService(new OrderService())
                    .build()
                    .start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    OrderServer.this.stopServer();
                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, "Server shutdown error", e);
                }
            }));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error starting the server", e);
        }
    }

    public void stopServer() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        OrderServer orderServer = new OrderServer();

        orderServer.startServer();
        orderServer.blockUntilShutdown();
    }

}
