package com.example.server;

import com.example.UserService;

import com.example.functional.ThrowingRunnable;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServer {

    private static final Logger logger = Logger.getLogger(UserServer.class.getName());

    private Server server;

    public void startServer() throws IOException {
        int port = 50005;

        server = ServerBuilder
                .forPort(port)
                .addService(new UserService())
                .build()
                .start();

        Runtime.getRuntime().addShutdownHook(
                new Thread(
                        ThrowingRunnable.handleThrowingRunnable(
                                UserServer.this::stopServer,
                                exception -> logger.log(Level.SEVERE, "Server shutdown error", exception.getMessage()))));

        logger.log(Level.INFO, "Server started at port " + port);
    }

    public void stopServer() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
            logger.log(Level.INFO, "Server stopped");
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        UserServer userServer = new UserServer();

        userServer.startServer();
        userServer.blockUntilShutdown();
    }
}
