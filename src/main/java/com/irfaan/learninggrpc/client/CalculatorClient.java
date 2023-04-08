/**
 * DANA
 * Copyright (c) 2018‐2023 All Rights Reserved.
 */
package com.irfaan.learninggrpc.client;

import com.proto.calculator.*;
import com.proto.greet.GreetEveryoneRequest;
import com.proto.greet.GreetEveryoneResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author irfaanhibatullah
 * @version $Id: CalculatorClient.java, v 0.1 2023‐04‐08 18.59 irfaanhibatullah Exp $$
 */
public class CalculatorClient {

    ManagedChannel channel;

    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC client");

        CalculatorClient client = new CalculatorClient();
        client.run();
    }

    private void run() {
        channel = ManagedChannelBuilder
                .forAddress("localhost", 12201)
                .usePlaintext()
                .build();


//        doUnaryCall(channel);
//        doStreamingServerCall(channel);
//        doClientStreamingCall(channel);
        doBiDiStreamingCall(channel);


        //shut down channel
        System.out.println("Shutting down Channel");
        channel.shutdown();

    }

    private void doBiDiStreamingCall(ManagedChannel channel) {
        var greetServiceStub = CalculatorServiceGrpc.newStub(channel);

        var latch = new CountDownLatch(1);

        var requestObserver = greetServiceStub.findMaximum(new StreamObserver<>() {
            @Override
            public void onNext(FindMaximumResponse value) {
                System.out.println("Get a new maxium from server : " + value.getMaximum());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server is done sending messages");
                latch.countDown();
            }
        });


        for (int i = 0; i < 200; i++) {
            var random = new Random();
            long value = random.nextLong();
            System.out.println("Send message: " + (i + 1) + " ,value : " + value);
            requestObserver.onNext(FindMaximumRequest
                    .newBuilder()
                    .setNumber(value)
                    .build());
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doClientStreamingCall(ManagedChannel channel) {
        var stub = CalculatorServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);
        var requestStreamObserver = stub.computeAverage(new StreamObserver<>() {
            @Override
            public void onNext(ComputeAverageResponse value) {
                //we get a response from  the server
                System.out.println("Received a response from the server");
                System.out.println("Result: " + value.getAverage());
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

        for (int i = 1; i <= 10000; i++) {
            //streaming message #1
            System.out.println("sending message : " + i);
            requestStreamObserver.onNext(ComputeAverageRequest
                    .newBuilder()
                    .setNumber(i)
                    .build());

        }

        //we tell the server that the client is done sending the data
        requestStreamObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doStreamingServerCall(ManagedChannel channel) {


        System.out.println("Creating stub");
        //created a greet service client(blocking - synchronous)
        var stub = CalculatorServiceGrpc.newBlockingStub(channel);


        //Streaming server
        var request = PrimeNumberDecompositionRequest.newBuilder()
                .setNumber(567890123123123123L).build();
        stub.primNumberDecomposition(request)
                .forEachRemaining(r -> System.out.println(r.getPrimeFactor()));

    }

    private void doUnaryCall(ManagedChannel channel) {

        System.out.println("Creating stub");
        //created a greet service client(blocking - synchronous)
        var stub = CalculatorServiceGrpc.newBlockingStub(channel);

        //created a protocol buffer greeting message
        SumRequest build = SumRequest.newBuilder()
                .setFirstNumber(200)
                .setSecondNumber(900)
                .build();

        //call the RPC and get back
        var greetResponse = stub.sum(build);
        System.out.println("sum result: " + greetResponse.getSumResult());
    }
}