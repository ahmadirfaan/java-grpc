/**
 * DANA
 * Copyright (c) 2018‐2023 All Rights Reserved.
 */
package com.irfaan.learninggrpc.client;

import com.irfaan.learning.javagrpc.DummyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * @author irfaanhibatullah
 * @version $Id: GreetingClient.java, v 0.1 2023‐04‐08 17.33 irfaanhibatullah Exp $$
 */
public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC client");

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 12200)
                .usePlaintext()
                .build();

        System.out.println("Creating stub");
        DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);

//        DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(channel);
//        Integer maxInboundMessageSize = syncClient.getCallOptions().getMaxInboundMessageSize();
        System.out.println(syncClient.getChannel());
        System.out.println("Shutting down channel");
//        channel.shutdown();
    }
}