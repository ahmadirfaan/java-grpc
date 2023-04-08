/**
 * DANA
 * Copyright (c) 2018‐2023 All Rights Reserved.
 */
package com.irfaan.learninggrpc.server;

import com.proto.greet.*;
import io.grpc.stub.StreamObserver;

/**
 * @author irfaanhibatullah
 * @version $Id: GreetServiceImpl.java, v 0.1 2023‐04‐08 18.36 irfaanhibatullah Exp $$
 */
public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        // extract the field from request
        var greeting = request.getGreeting();
        var firstName = greeting.getFirstName();
        var lastName = greeting.getLastName();

        //create the response
        String result = String.format("Hello %s %s", firstName, lastName);
        GreetResponse response = GreetResponse.newBuilder()
                .setResult(result).build();

        //send the response
        responseObserver.onNext(response);

        //complete the RPC Call
        responseObserver.onCompleted();
    }

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {

        var firstName = request.getGreeting().getFirstName();
        var lastName = request.getGreeting().getLastName();


        try {
            for (int i = 0; i < 10; i++) {
                var result = String.format("Hello %s %s, response number %s", firstName, lastName, i);
                var response = GreetManyTimesResponse.newBuilder()
                        .setResult(result)
                        .build();
                responseObserver.onNext(response);
                Thread.sleep(1000L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
        return new StreamObserver<>() {

            String result = "";

            @Override
            public void onNext(LongGreetRequest value) {
                //client sends a message
                result += ". Hello " + value.getGreeting().getFirstName() + " " + value.getGreeting().getLastName() + "! ";
            }

            @Override
            public void onError(Throwable t) {
                //client sends an error
            }

            @Override
            public void onCompleted() {
                //client is done
                responseObserver.onNext(LongGreetResponse
                        .newBuilder()
                        .setResult(result)
                        .build());

                responseObserver.onCompleted();
                //this is when we want to return response (responseObserver)
            }
        };
    }

    @Override
    public StreamObserver<GreetEveryoneRequest> greetEveryone(StreamObserver<GreetEveryoneResponse> responseObserver) {

        return new StreamObserver<>() {
            @Override
            public void onNext(GreetEveryoneRequest value) {
                var response = "Hello " + value.getGreeting().getFirstName() + " " + value.getGreeting().getLastName();
                GreetEveryoneResponse everyoneResponse = GreetEveryoneResponse.newBuilder()
                        .setResult(response)
                        .build();

                responseObserver.onNext(everyoneResponse);
            }

            @Override
            public void onError(Throwable t) {
                //do Nothing
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}