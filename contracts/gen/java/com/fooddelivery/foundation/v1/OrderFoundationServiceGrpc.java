package com.fooddelivery.foundation.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class OrderFoundationServiceGrpc {

  private OrderFoundationServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "fooddelivery.foundation.v1.OrderFoundationService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.foundation.v1.CustomerPingRequest,
      com.fooddelivery.foundation.v1.CustomerPingResponse> getCustomerPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CustomerPing",
      requestType = com.fooddelivery.foundation.v1.CustomerPingRequest.class,
      responseType = com.fooddelivery.foundation.v1.CustomerPingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.foundation.v1.CustomerPingRequest,
      com.fooddelivery.foundation.v1.CustomerPingResponse> getCustomerPingMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.foundation.v1.CustomerPingRequest, com.fooddelivery.foundation.v1.CustomerPingResponse> getCustomerPingMethod;
    if ((getCustomerPingMethod = OrderFoundationServiceGrpc.getCustomerPingMethod) == null) {
      synchronized (OrderFoundationServiceGrpc.class) {
        if ((getCustomerPingMethod = OrderFoundationServiceGrpc.getCustomerPingMethod) == null) {
          OrderFoundationServiceGrpc.getCustomerPingMethod = getCustomerPingMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.foundation.v1.CustomerPingRequest, com.fooddelivery.foundation.v1.CustomerPingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CustomerPing"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.foundation.v1.CustomerPingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.foundation.v1.CustomerPingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OrderFoundationServiceMethodDescriptorSupplier("CustomerPing"))
              .build();
        }
      }
    }
    return getCustomerPingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.foundation.v1.AdminPingRequest,
      com.fooddelivery.foundation.v1.AdminPingResponse> getAdminPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AdminPing",
      requestType = com.fooddelivery.foundation.v1.AdminPingRequest.class,
      responseType = com.fooddelivery.foundation.v1.AdminPingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.foundation.v1.AdminPingRequest,
      com.fooddelivery.foundation.v1.AdminPingResponse> getAdminPingMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.foundation.v1.AdminPingRequest, com.fooddelivery.foundation.v1.AdminPingResponse> getAdminPingMethod;
    if ((getAdminPingMethod = OrderFoundationServiceGrpc.getAdminPingMethod) == null) {
      synchronized (OrderFoundationServiceGrpc.class) {
        if ((getAdminPingMethod = OrderFoundationServiceGrpc.getAdminPingMethod) == null) {
          OrderFoundationServiceGrpc.getAdminPingMethod = getAdminPingMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.foundation.v1.AdminPingRequest, com.fooddelivery.foundation.v1.AdminPingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AdminPing"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.foundation.v1.AdminPingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.foundation.v1.AdminPingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OrderFoundationServiceMethodDescriptorSupplier("AdminPing"))
              .build();
        }
      }
    }
    return getAdminPingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static OrderFoundationServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrderFoundationServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrderFoundationServiceStub>() {
        @java.lang.Override
        public OrderFoundationServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrderFoundationServiceStub(channel, callOptions);
        }
      };
    return OrderFoundationServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static OrderFoundationServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrderFoundationServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrderFoundationServiceBlockingV2Stub>() {
        @java.lang.Override
        public OrderFoundationServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrderFoundationServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return OrderFoundationServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static OrderFoundationServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrderFoundationServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrderFoundationServiceBlockingStub>() {
        @java.lang.Override
        public OrderFoundationServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrderFoundationServiceBlockingStub(channel, callOptions);
        }
      };
    return OrderFoundationServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static OrderFoundationServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrderFoundationServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrderFoundationServiceFutureStub>() {
        @java.lang.Override
        public OrderFoundationServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrderFoundationServiceFutureStub(channel, callOptions);
        }
      };
    return OrderFoundationServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void customerPing(com.fooddelivery.foundation.v1.CustomerPingRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.foundation.v1.CustomerPingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCustomerPingMethod(), responseObserver);
    }

    /**
     */
    default void adminPing(com.fooddelivery.foundation.v1.AdminPingRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.foundation.v1.AdminPingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAdminPingMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service OrderFoundationService.
   */
  public static abstract class OrderFoundationServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return OrderFoundationServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service OrderFoundationService.
   */
  public static final class OrderFoundationServiceStub
      extends io.grpc.stub.AbstractAsyncStub<OrderFoundationServiceStub> {
    private OrderFoundationServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderFoundationServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrderFoundationServiceStub(channel, callOptions);
    }

    /**
     */
    public void customerPing(com.fooddelivery.foundation.v1.CustomerPingRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.foundation.v1.CustomerPingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCustomerPingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void adminPing(com.fooddelivery.foundation.v1.AdminPingRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.foundation.v1.AdminPingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAdminPingMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service OrderFoundationService.
   */
  public static final class OrderFoundationServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<OrderFoundationServiceBlockingV2Stub> {
    private OrderFoundationServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderFoundationServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrderFoundationServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public com.fooddelivery.foundation.v1.CustomerPingResponse customerPing(com.fooddelivery.foundation.v1.CustomerPingRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCustomerPingMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.foundation.v1.AdminPingResponse adminPing(com.fooddelivery.foundation.v1.AdminPingRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getAdminPingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service OrderFoundationService.
   */
  public static final class OrderFoundationServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<OrderFoundationServiceBlockingStub> {
    private OrderFoundationServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderFoundationServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrderFoundationServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.fooddelivery.foundation.v1.CustomerPingResponse customerPing(com.fooddelivery.foundation.v1.CustomerPingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCustomerPingMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.foundation.v1.AdminPingResponse adminPing(com.fooddelivery.foundation.v1.AdminPingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAdminPingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service OrderFoundationService.
   */
  public static final class OrderFoundationServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<OrderFoundationServiceFutureStub> {
    private OrderFoundationServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderFoundationServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrderFoundationServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.foundation.v1.CustomerPingResponse> customerPing(
        com.fooddelivery.foundation.v1.CustomerPingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCustomerPingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.foundation.v1.AdminPingResponse> adminPing(
        com.fooddelivery.foundation.v1.AdminPingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAdminPingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CUSTOMER_PING = 0;
  private static final int METHODID_ADMIN_PING = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CUSTOMER_PING:
          serviceImpl.customerPing((com.fooddelivery.foundation.v1.CustomerPingRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.foundation.v1.CustomerPingResponse>) responseObserver);
          break;
        case METHODID_ADMIN_PING:
          serviceImpl.adminPing((com.fooddelivery.foundation.v1.AdminPingRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.foundation.v1.AdminPingResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getCustomerPingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.foundation.v1.CustomerPingRequest,
              com.fooddelivery.foundation.v1.CustomerPingResponse>(
                service, METHODID_CUSTOMER_PING)))
        .addMethod(
          getAdminPingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.foundation.v1.AdminPingRequest,
              com.fooddelivery.foundation.v1.AdminPingResponse>(
                service, METHODID_ADMIN_PING)))
        .build();
  }

  private static abstract class OrderFoundationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    OrderFoundationServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.fooddelivery.foundation.v1.Foundation.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("OrderFoundationService");
    }
  }

  private static final class OrderFoundationServiceFileDescriptorSupplier
      extends OrderFoundationServiceBaseDescriptorSupplier {
    OrderFoundationServiceFileDescriptorSupplier() {}
  }

  private static final class OrderFoundationServiceMethodDescriptorSupplier
      extends OrderFoundationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    OrderFoundationServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (OrderFoundationServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new OrderFoundationServiceFileDescriptorSupplier())
              .addMethod(getCustomerPingMethod())
              .addMethod(getAdminPingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
