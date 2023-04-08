/**
 * DANA
 * Copyright (c) 2018‐2023 All Rights Reserved.
 */
package com.irfaan.learninggrpc.server;

import com.proto.calculator.*;
import io.grpc.stub.StreamObserver;

/**
 * @author irfaanhibatullah
 * @version $Id: CalculatorServiceImpl.java, v 0.1 2023‐04‐08 18.58 irfaanhibatullah Exp $$
 */
public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        var response = SumResponse.newBuilder()
                .setSumResult(request.getFirstNumber() + request.getSecondNumber())
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    @Override
    public void primNumberDecomposition(PrimeNumberDecompositionRequest request, StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
        long number = request.getNumber();
        long divisior = 2;
        while (number > 1) {
            if (number % divisior == 0) {
                number = number / divisior;
                responseObserver.onNext(PrimeNumberDecompositionResponse.newBuilder()
                        .setPrimeFactor(divisior).build());
            } else {
                divisior += 1;
            }
        }

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<ComputeAverageRequest> computeAverage(StreamObserver<ComputeAverageResponse> responseObserver) {
        return new StreamObserver<>() {
            int sum = 0;
            int count = 0;

            @Override
            public void onNext(ComputeAverageRequest value) {
                //increment the sum and count
                sum += value.getNumber();
                count++;
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                double average = (double) sum / count;
                responseObserver.onNext(ComputeAverageResponse
                        .newBuilder()
                        .setAverage(average)
                        .build());

                responseObserver.onCompleted();
            }
        };
    }
}