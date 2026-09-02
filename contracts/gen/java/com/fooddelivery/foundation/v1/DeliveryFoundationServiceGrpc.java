package com.fooddelivery.foundation.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class DeliveryFoundationServiceGrpc {

  private DeliveryFoundationServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "fooddelivery.foundation.v1.DeliveryFoundationService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.foundation.v1.CourierPingRequest,
      com.fooddelivery.foundation.v1.CourierPingResponse> getCourierPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CourierPing",
      requestType = com.fooddelivery.foundation.v1.CourierPingRequest.class,
      responseType = com.fooddelivery.foundation.v1.CourierPingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.foundation.v1.CourierPingRequest,
      com.fooddelivery.foundation.v1.CourierPingResponse> getCourierPingMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.foundation.v1.CourierPingRequest, com.fooddelivery.foundation.v1.CourierPingResponse> getCourierPingMethod;
    if ((getCourierPingMethod = DeliveryFoundationServiceGrpc.getCourierPingMethod) == null) {
      synchronized (DeliveryFoundationServiceGrpc.class) {
        if ((getCourierPingMethod = DeliveryFoundationServiceGrpc.getCourierPingMethod) == null) {
          DeliveryFoundationServiceGrpc.getCourierPingMethod = getCourierPingMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.foundation.v1.CourierPingRequest, com.fooddelivery.foundation.v1.CourierPingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CourierPing"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.foundation.v1.CourierPingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.foundation.v1.CourierPingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DeliveryFoundationServiceMethodDescriptorSupplier("CourierPing"))
              .build();
        }
      }
    }
    return getCourierPingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DeliveryFoundationServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeliveryFoundationServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeliveryFoundationServiceStub>() {
        @java.lang.Override
        public DeliveryFoundationServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeliveryFoundationServiceStub(channel, callOptions);
        }
      };
    return DeliveryFoundationServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static DeliveryFoundationServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeliveryFoundationServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeliveryFoundationServiceBlockingV2Stub>() {
        @java.lang.Override
        public DeliveryFoundationServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeliveryFoundationServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return DeliveryFoundationServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DeliveryFoundationServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeliveryFoundationServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeliveryFoundationServiceBlockingStub>() {
        @java.lang.Override
        public DeliveryFoundationServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeliveryFoundationServiceBlockingStub(channel, callOptions);
        }
      };
    return DeliveryFoundationServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DeliveryFoundationServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeliveryFoundationServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeliveryFoundationServiceFutureStub>() {
        @java.lang.Override
        public DeliveryFoundationServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeliveryFoundationServiceFutureStub(channel, callOptions);
        }
      };
    return DeliveryFoundationServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void courierPing(com.fooddelivery.foundation.v1.CourierPingRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.foundation.v1.CourierPingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCourierPingMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DeliveryFoundationService.
   */
  public static abstract class DeliveryFoundationServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DeliveryFoundationServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DeliveryFoundationService.
   */
  public static final class DeliveryFoundationServiceStub
      extends io.grpc.stub.AbstractAsyncStub<DeliveryFoundationServiceStub> {
    private DeliveryFoundationServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeliveryFoundationServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeliveryFoundationServiceStub(channel, callOptions);
    }

    /**
     */
    public void courierPing(com.fooddelivery.foundation.v1.CourierPingRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.foundation.v1.CourierPingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCourierPingMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DeliveryFoundationService.
   */
  public static final class DeliveryFoundationServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<DeliveryFoundationServiceBlockingV2Stub> {
    private DeliveryFoundationServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeliveryFoundationServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeliveryFoundationServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public com.fooddelivery.foundation.v1.CourierPingResponse courierPing(com.fooddelivery.foundation.v1.CourierPingRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCourierPingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service DeliveryFoundationService.
   */
  public static final class DeliveryFoundationServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DeliveryFoundationServiceBlockingStub> {
    private DeliveryFoundationServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeliveryFoundationServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeliveryFoundationServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.fooddelivery.foundation.v1.CourierPingResponse courierPing(com.fooddelivery.foundation.v1.CourierPingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCourierPingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DeliveryFoundationService.
   */
  public static final class DeliveryFoundationServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<DeliveryFoundationServiceFutureStub> {
    private DeliveryFoundationServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeliveryFoundationServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeliveryFoundationServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.foundation.v1.CourierPingResponse> courierPing(
        com.fooddelivery.foundation.v1.CourierPingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCourierPingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_COURIER_PING = 0;

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
        case METHODID_COURIER_PING:
          serviceImpl.courierPing((com.fooddelivery.foundation.v1.CourierPingRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.foundation.v1.CourierPingResponse>) responseObserver);
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
          getCourierPingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.foundation.v1.CourierPingRequest,
              com.fooddelivery.foundation.v1.CourierPingResponse>(
                service, METHODID_COURIER_PING)))
        .build();
  }

  private static abstract class DeliveryFoundationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DeliveryFoundationServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.fooddelivery.foundation.v1.Foundation.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DeliveryFoundationService");
    }
  }

  private static final class DeliveryFoundationServiceFileDescriptorSupplier
      extends DeliveryFoundationServiceBaseDescriptorSupplier {
    DeliveryFoundationServiceFileDescriptorSupplier() {}
  }

  private static final class DeliveryFoundationServiceMethodDescriptorSupplier
      extends DeliveryFoundationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DeliveryFoundationServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (DeliveryFoundationServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DeliveryFoundationServiceFileDescriptorSupplier())
              .addMethod(getCourierPingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
