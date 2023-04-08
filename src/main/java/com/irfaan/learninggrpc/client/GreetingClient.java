/**
 * DANA
 * Copyright (c) 2018‐2023 All Rights Reserved.
 */
package com.irfaan.learninggrpc.client;

import com.irfaan.learning.javagrpc.DummyServiceGrpc;
import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author irfaanhibatullah
 * @version $Id: GreetingClient.java, v 0.1 2023‐04‐08 17.33 irfaanhibatullah Exp $$
 */
public class GreetingClient {

    ManagedChannel channel;


    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC client");

        GreetingClient client = new GreetingClient();
        client.run();

        System.out.println("Creating stub");
//
    }


    public void run() {
        channel = ManagedChannelBuilder
                .forAddress("localhost", 12200)
                .usePlaintext()
                .build();
//
//        doUnaryCall(channel);
//        doServerStreamingCall(channel);
//        doClientStreamingCall(channel);
        doBiDiStreamingCall(channel);


        //shut down channel
        System.out.println("Shutting down Channel");

        channel.shutdown();
    }

    private void doBiDiStreamingCall(ManagedChannel channel) {
        var greetServiceStub = GreetServiceGrpc.newStub(channel);

        var latch = new CountDownLatch(1);

        var requestObserver = greetServiceStub.greetEveryone(new StreamObserver<>() {
            @Override
            public void onNext(GreetEveryoneResponse value) {
                System.out.println("Received response from a server : " + value.getResult());

            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server is done sending data");
                latch.countDown();
            }
        });


        Arrays.asList(List.of("Ahmad", "Irfaan"), List.of("Tuti", "Wulandari"), List.of("Ahmad", "Afiif"))
                .forEach(s -> requestObserver.onNext(GreetEveryoneRequest
                        .newBuilder()
                        .setGreeting(Greeting.newBuilder()
                                .setFirstName(s.get(0))
                                .setLastName(s.get(1)).build())
                        .build()));

        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void doClientStreamingCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceStub greetServiceStub = GreetServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);
        var longGreetRequestStreamObserver = greetServiceStub.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse value) {
                //we get a response from  the server
                System.out.println("Received a response from the server");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                //we get an error from the server

            }

            @Override
            public void onCompleted() {
                //the server is done sending us data
                //onCompleted will be called right after onNext()
                System.out.println("Server has completed sending us something");
                latch.countDown();
            }
        });

        System.out.println("sending message 1");
        //streaming message #1
        longGreetRequestStreamObserver.onNext(LongGreetRequest
                .newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Ahmad")
                        .setLastName("Irfaan"))
                .build());

        System.out.println("sending message 2");
        //streaming message #2
        longGreetRequestStreamObserver.onNext(LongGreetRequest
                .newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Tuti ")
                        .setLastName("Wulandari"))
                .build());

        System.out.println("sending message 3");
        //streaming message #3
        longGreetRequestStreamObserver.onNext(LongGreetRequest
                .newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Ahmad")
                        .setLastName("Afiif"))
                .build());

        //we tell the server that the client is done sending the data
        longGreetRequestStreamObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void doServerStreamingCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        var greeting = Greeting.newBuilder()
                .setFirstName("Ahmad Irfaan")
                .setLastName("Hibatullah")
                .build();

        var greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        //streaming API gRPC
        Iterator<GreetManyTimesResponse> responseIterator = greetClient.greetManyTimes(greetManyTimesRequest);
        responseIterator.forEachRemaining(response -> System.out.println(response.getResult()));
    }

    private void doUnaryCall(ManagedChannel channel) {
        DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);

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
    }
}