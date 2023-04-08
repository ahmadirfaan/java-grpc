/**
 * DANA
 * Copyright (c) 2018‐2023 All Rights Reserved.
 */
package com.irfaan.learninggrpc.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.PrimeNumberDecompositionRequest;
import com.proto.calculator.PrimeNumberDecompositionResponse;
import com.proto.calculator.SumRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;

/**
 * @author irfaanhibatullah
 * @version $Id: CalculatorClient.java, v 0.1 2023‐04‐08 18.59 irfaanhibatullah Exp $$
 */
public class CalculatorClient {

 public static void main(String[] args) {
  System.out.println("Hello I'm a gRPC client");

  ManagedChannel channel = ManagedChannelBuilder
          .forAddress("localhost", 12201)
          .usePlaintext()
          .build();

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

  //Streaming server
  var request = PrimeNumberDecompositionRequest.newBuilder()
          .setNumber(567890123123123123L).build();
  stub.primNumberDecomposition(request)
          .forEachRemaining(r -> System.out.println(r.getPrimeFactor()));

  //shut down channel
  System.out.println("Shutting down Channel");
  channel.shutdown();
 }
}