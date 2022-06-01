package com.example.server;

import com.example.CalcService;
import com.example.functional.ThrowingRunnable;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CalcServer {

    private Server server;

    private void startServer() throws IOException {
        int port = 50010;

        server = ServerBuilder
                .forPort(port)
                .addService(new CalcService())
                .build()
                .start();

        Runtime.getRuntime().addShutdownHook(
                new Thread(
                        ThrowingRunnable.handleThrowingRunnable(
                                CalcServer.this::stopServer,
                                (ex) -> System.out.println("Error happened + " + ex))));
    }

    private void stopServer() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(10, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        CalcServer server = new CalcServer();

        server.startServer();
        server.blockUntilShutdown();
    }
}
