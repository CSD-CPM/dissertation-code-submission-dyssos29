package com.fooddelivery.order_service.grpc;

import com.fooddelivery.foundation.v1.AdminPingRequest;
import com.fooddelivery.foundation.v1.AdminPingResponse;
import com.fooddelivery.foundation.v1.CustomerPingRequest;
import com.fooddelivery.foundation.v1.CustomerPingResponse;
import com.fooddelivery.foundation.v1.OrderFoundationServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import com.fooddelivery.order_service.auth.RoleGuard;
import com.fooddelivery.order_service.auth.Role;

@GrpcService
public class OrderFoundationGrpcService
        extends OrderFoundationServiceGrpc.OrderFoundationServiceImplBase {

    @Override
    public void customerPing(
            CustomerPingRequest request,
            StreamObserver<CustomerPingResponse> responseObserver) {
        RoleGuard.requireRole(Role.CUSTOMER);

        CustomerPingResponse response =
                CustomerPingResponse.newBuilder()
                        .setService("order-service")
                        .setMessage("Foundation probe reachable")
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void adminPing(
            AdminPingRequest request,
            StreamObserver<AdminPingResponse> responseObserver) {
        RoleGuard.requireRole(Role.ADMIN);

        AdminPingResponse response =
                AdminPingResponse.newBuilder()
                        .setService("order-service")
                        .setMessage("Foundation probe reachable")
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
