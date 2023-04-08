/**
 * DANA
 * Copyright (c) 2018‐2023 All Rights Reserved.
 */
package com.irfaan.learninggrpc.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;

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
//        DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);

//        DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(channel);
//        Integer maxInboundMessageSize = syncClient.getCallOptions().getMaxInboundMessageSize();

        //created a greet service client(blocking - synchronous)
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        //created a protocol buffer greeting message
        var greeting = Greeting.newBuilder()
                .setFirstName("Ahmad Irfaan")
                .setLastName("Hibatullah")
                .build();
        var greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        //call the RPC and get back
        var greetResponse = greetClient.greet(greetRequest);
        System.out.println(greetResponse.getResult());

        var greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        //streaming API gRPC
        Iterator<GreetManyTimesResponse> responseIterator = greetClient.greetManyTimes(greetManyTimesRequest);
        responseIterator.forEachRemaining(response -> System.out.println(response.getResult()));

        //shut down channel
        System.out.println("Shutting down Channel");
        channel.shutdown();
    }
}