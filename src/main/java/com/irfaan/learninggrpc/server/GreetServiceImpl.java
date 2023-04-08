/**
 * DANA
 * Copyright (c) 2018‐2023 All Rights Reserved.
 */
package com.irfaan.learninggrpc.server;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
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
}