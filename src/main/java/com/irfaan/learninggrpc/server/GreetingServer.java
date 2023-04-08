package com.irfaan.learninggrpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;


public class GreetingServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(12200).build();
        server.start();
        System.out.println("Server Start");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received Shutdown Request");
            server.shutdown();
            System.out.println("Successfully stopped the server");
        }));

        server.awaitTermination();
    }

}
