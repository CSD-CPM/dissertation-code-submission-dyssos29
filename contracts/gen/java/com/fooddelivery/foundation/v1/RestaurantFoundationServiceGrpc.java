package com.fooddelivery.foundation.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class RestaurantFoundationServiceGrpc {

  private RestaurantFoundationServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "fooddelivery.foundation.v1.RestaurantFoundationService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.foundation.v1.RestaurantPingRequest,
      com.fooddelivery.foundation.v1.RestaurantPingResponse> getRestaurantPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RestaurantPing",
      requestType = com.fooddelivery.foundation.v1.RestaurantPingRequest.class,
      responseType = com.fooddelivery.foundation.v1.RestaurantPingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.foundation.v1.RestaurantPingRequest,
      com.fooddelivery.foundation.v1.RestaurantPingResponse> getRestaurantPingMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.foundation.v1.RestaurantPingRequest, com.fooddelivery.foundation.v1.RestaurantPingResponse> getRestaurantPingMethod;
    if ((getRestaurantPingMethod = RestaurantFoundationServiceGrpc.getRestaurantPingMethod) == null) {
      synchronized (RestaurantFoundationServiceGrpc.class) {
        if ((getRestaurantPingMethod = RestaurantFoundationServiceGrpc.getRestaurantPingMethod) == null) {
          RestaurantFoundationServiceGrpc.getRestaurantPingMethod = getRestaurantPingMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.foundation.v1.RestaurantPingRequest, com.fooddelivery.foundation.v1.RestaurantPingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RestaurantPing"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.foundation.v1.RestaurantPingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.foundation.v1.RestaurantPingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RestaurantFoundationServiceMethodDescriptorSupplier("RestaurantPing"))
              .build();
        }
      }
    }
    return getRestaurantPingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RestaurantFoundationServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RestaurantFoundationServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RestaurantFoundationServiceStub>() {
        @java.lang.Override
        public RestaurantFoundationServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RestaurantFoundationServiceStub(channel, callOptions);
        }
      };
    return RestaurantFoundationServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static RestaurantFoundationServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RestaurantFoundationServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RestaurantFoundationServiceBlockingV2Stub>() {
        @java.lang.Override
        public RestaurantFoundationServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RestaurantFoundationServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return RestaurantFoundationServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RestaurantFoundationServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RestaurantFoundationServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RestaurantFoundationServiceBlockingStub>() {
        @java.lang.Override
        public RestaurantFoundationServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RestaurantFoundationServiceBlockingStub(channel, callOptions);
        }
      };
    return RestaurantFoundationServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RestaurantFoundationServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RestaurantFoundationServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RestaurantFoundationServiceFutureStub>() {
        @java.lang.Override
        public RestaurantFoundationServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RestaurantFoundationServiceFutureStub(channel, callOptions);
        }
      };
    return RestaurantFoundationServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void restaurantPing(com.fooddelivery.foundation.v1.RestaurantPingRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.foundation.v1.RestaurantPingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRestaurantPingMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service RestaurantFoundationService.
   */
  public static abstract class RestaurantFoundationServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return RestaurantFoundationServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service RestaurantFoundationService.
   */
  public static final class RestaurantFoundationServiceStub
      extends io.grpc.stub.AbstractAsyncStub<RestaurantFoundationServiceStub> {
    private RestaurantFoundationServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RestaurantFoundationServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RestaurantFoundationServiceStub(channel, callOptions);
    }

    /**
     */
    public void restaurantPing(com.fooddelivery.foundation.v1.RestaurantPingRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.foundation.v1.RestaurantPingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRestaurantPingMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service RestaurantFoundationService.
   */
  public static final class RestaurantFoundationServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<RestaurantFoundationServiceBlockingV2Stub> {
    private RestaurantFoundationServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RestaurantFoundationServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RestaurantFoundationServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public com.fooddelivery.foundation.v1.RestaurantPingResponse restaurantPing(com.fooddelivery.foundation.v1.RestaurantPingRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getRestaurantPingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service RestaurantFoundationService.
   */
  public static final class RestaurantFoundationServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<RestaurantFoundationServiceBlockingStub> {
    private RestaurantFoundationServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RestaurantFoundationServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RestaurantFoundationServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.fooddelivery.foundation.v1.RestaurantPingResponse restaurantPing(com.fooddelivery.foundation.v1.RestaurantPingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRestaurantPingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service RestaurantFoundationService.
   */
  public static final class RestaurantFoundationServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<RestaurantFoundationServiceFutureStub> {
    private RestaurantFoundationServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RestaurantFoundationServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RestaurantFoundationServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.foundation.v1.RestaurantPingResponse> restaurantPing(
        com.fooddelivery.foundation.v1.RestaurantPingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRestaurantPingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_RESTAURANT_PING = 0;

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
        case METHODID_RESTAURANT_PING:
          serviceImpl.restaurantPing((com.fooddelivery.foundation.v1.RestaurantPingRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.foundation.v1.RestaurantPingResponse>) responseObserver);
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
          getRestaurantPingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.foundation.v1.RestaurantPingRequest,
              com.fooddelivery.foundation.v1.RestaurantPingResponse>(
                service, METHODID_RESTAURANT_PING)))
        .build();
  }

  private static abstract class RestaurantFoundationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RestaurantFoundationServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.fooddelivery.foundation.v1.Foundation.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RestaurantFoundationService");
    }
  }

  private static final class RestaurantFoundationServiceFileDescriptorSupplier
      extends RestaurantFoundationServiceBaseDescriptorSupplier {
    RestaurantFoundationServiceFileDescriptorSupplier() {}
  }

  private static final class RestaurantFoundationServiceMethodDescriptorSupplier
      extends RestaurantFoundationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    RestaurantFoundationServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (RestaurantFoundationServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RestaurantFoundationServiceFileDescriptorSupplier())
              .addMethod(getRestaurantPingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
