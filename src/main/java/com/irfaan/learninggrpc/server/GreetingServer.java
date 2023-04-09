package com.irfaan.learninggrpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.File;
import java.io.IOException;


public class GreetingServer {

    public static void main(String[] args) throws IOException, InterruptedException {

        //plain text server
//        Server server = ServerBuilder.forPort(12200)
//                .addService(new GreetServiceImpl())
//                .build();
//        server.start();
        System.out.println("Server Start");

        //secure server
        Server server = ServerBuilder.forPort(12200)
                .addService(new GreetServiceImpl())
                .useTransportSecurity(
                        new File("ssl/server.crt"),
                        new File("ssl/server.pem")
                )
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received Shutdown Request");
            server.shutdown();
            System.out.println("Successfully stopped the server");
        }));

        server.awaitTermination();
    }

}
