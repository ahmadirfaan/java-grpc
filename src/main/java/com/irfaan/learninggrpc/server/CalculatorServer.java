/**
 * DANA
 * Copyright (c) 2018‐2023 All Rights Reserved.
 */
package com.irfaan.learninggrpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * @author irfaanhibatullah
 * @version $Id: CalculatorServer.java, v 0.1 2023‐04‐08 18.55 irfaanhibatullah Exp $$
 */
public class CalculatorServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(12201)
                .addService(new CalculatorServiceImpl())
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