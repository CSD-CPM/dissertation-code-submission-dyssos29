package com.fooddelivery.foundation.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class FoundationServiceGrpc {

  private FoundationServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "fooddelivery.foundation.v1.FoundationService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.foundation.v1.PingRequest,
      com.fooddelivery.foundation.v1.PingResponse> getPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Ping",
      requestType = com.fooddelivery.foundation.v1.PingRequest.class,
      responseType = com.fooddelivery.foundation.v1.PingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.foundation.v1.PingRequest,
      com.fooddelivery.foundation.v1.PingResponse> getPingMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.foundation.v1.PingRequest, com.fooddelivery.foundation.v1.PingResponse> getPingMethod;
    if ((getPingMethod = FoundationServiceGrpc.getPingMethod) == null) {
      synchronized (FoundationServiceGrpc.class) {
        if ((getPingMethod = FoundationServiceGrpc.getPingMethod) == null) {
          FoundationServiceGrpc.getPingMethod = getPingMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.foundation.v1.PingRequest, com.fooddelivery.foundation.v1.PingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.foundation.v1.PingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.foundation.v1.PingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FoundationServiceMethodDescriptorSupplier("Ping"))
              .build();
        }
      }
    }
    return getPingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FoundationServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FoundationServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FoundationServiceStub>() {
        @java.lang.Override
        public FoundationServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FoundationServiceStub(channel, callOptions);
        }
      };
    return FoundationServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static FoundationServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FoundationServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FoundationServiceBlockingV2Stub>() {
        @java.lang.Override
        public FoundationServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FoundationServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return FoundationServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FoundationServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FoundationServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FoundationServiceBlockingStub>() {
        @java.lang.Override
        public FoundationServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FoundationServiceBlockingStub(channel, callOptions);
        }
      };
    return FoundationServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FoundationServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FoundationServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FoundationServiceFutureStub>() {
        @java.lang.Override
        public FoundationServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FoundationServiceFutureStub(channel, callOptions);
        }
      };
    return FoundationServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void ping(com.fooddelivery.foundation.v1.PingRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.foundation.v1.PingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service FoundationService.
   */
  public static abstract class FoundationServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return FoundationServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service FoundationService.
   */
  public static final class FoundationServiceStub
      extends io.grpc.stub.AbstractAsyncStub<FoundationServiceStub> {
    private FoundationServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FoundationServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FoundationServiceStub(channel, callOptions);
    }

    /**
     */
    public void ping(com.fooddelivery.foundation.v1.PingRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.foundation.v1.PingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service FoundationService.
   */
  public static final class FoundationServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<FoundationServiceBlockingV2Stub> {
    private FoundationServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FoundationServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FoundationServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public com.fooddelivery.foundation.v1.PingResponse ping(com.fooddelivery.foundation.v1.PingRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service FoundationService.
   */
  public static final class FoundationServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<FoundationServiceBlockingStub> {
    private FoundationServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FoundationServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FoundationServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.fooddelivery.foundation.v1.PingResponse ping(com.fooddelivery.foundation.v1.PingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service FoundationService.
   */
  public static final class FoundationServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<FoundationServiceFutureStub> {
    private FoundationServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FoundationServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FoundationServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.foundation.v1.PingResponse> ping(
        com.fooddelivery.foundation.v1.PingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PING = 0;

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
        case METHODID_PING:
          serviceImpl.ping((com.fooddelivery.foundation.v1.PingRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.foundation.v1.PingResponse>) responseObserver);
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
          getPingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.foundation.v1.PingRequest,
              com.fooddelivery.foundation.v1.PingResponse>(
                service, METHODID_PING)))
        .build();
  }

  private static abstract class FoundationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FoundationServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.fooddelivery.foundation.v1.Foundation.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("FoundationService");
    }
  }

  private static final class FoundationServiceFileDescriptorSupplier
      extends FoundationServiceBaseDescriptorSupplier {
    FoundationServiceFileDescriptorSupplier() {}
  }

  private static final class FoundationServiceMethodDescriptorSupplier
      extends FoundationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    FoundationServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (FoundationServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FoundationServiceFileDescriptorSupplier())
              .addMethod(getPingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
