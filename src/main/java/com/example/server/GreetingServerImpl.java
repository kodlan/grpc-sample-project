package com.example.server;

import com.example.GreeterImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GreetingServerImpl {

    private static final Logger logger = Logger.getLogger(GreetingServerImpl.class.getName());

    private Server server;

    public void startServer() {
        int port = 50001;

        try {
            server = ServerBuilder
                    .forPort(port)
                    .addService(new GreeterImpl())
                    .build()
                    .start();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        GreetingServerImpl.this.stopServer();
                    } catch (InterruptedException e) {
                        logger.log(Level.SEVERE, "Server shutdown error", e);
                    }
                }
            });
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
        GreetingServerImpl greetingServer = new GreetingServerImpl();

        greetingServer.startServer();
        greetingServer.blockUntilShutdown();
    }

}
