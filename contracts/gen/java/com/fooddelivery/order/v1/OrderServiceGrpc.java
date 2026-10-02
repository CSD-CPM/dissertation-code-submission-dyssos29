package com.fooddelivery.order.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class OrderServiceGrpc {

  private OrderServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "fooddelivery.order.v1.OrderService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.order.v1.CreateOrderRequest,
      com.fooddelivery.order.v1.CreateOrderResponse> getCreateOrderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateOrder",
      requestType = com.fooddelivery.order.v1.CreateOrderRequest.class,
      responseType = com.fooddelivery.order.v1.CreateOrderResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.order.v1.CreateOrderRequest,
      com.fooddelivery.order.v1.CreateOrderResponse> getCreateOrderMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.order.v1.CreateOrderRequest, com.fooddelivery.order.v1.CreateOrderResponse> getCreateOrderMethod;
    if ((getCreateOrderMethod = OrderServiceGrpc.getCreateOrderMethod) == null) {
      synchronized (OrderServiceGrpc.class) {
        if ((getCreateOrderMethod = OrderServiceGrpc.getCreateOrderMethod) == null) {
          OrderServiceGrpc.getCreateOrderMethod = getCreateOrderMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.order.v1.CreateOrderRequest, com.fooddelivery.order.v1.CreateOrderResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateOrder"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.order.v1.CreateOrderRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.order.v1.CreateOrderResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OrderServiceMethodDescriptorSupplier("CreateOrder"))
              .build();
        }
      }
    }
    return getCreateOrderMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.order.v1.GetOrderRequest,
      com.fooddelivery.order.v1.GetOrderResponse> getGetOrderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetOrder",
      requestType = com.fooddelivery.order.v1.GetOrderRequest.class,
      responseType = com.fooddelivery.order.v1.GetOrderResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.order.v1.GetOrderRequest,
      com.fooddelivery.order.v1.GetOrderResponse> getGetOrderMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.order.v1.GetOrderRequest, com.fooddelivery.order.v1.GetOrderResponse> getGetOrderMethod;
    if ((getGetOrderMethod = OrderServiceGrpc.getGetOrderMethod) == null) {
      synchronized (OrderServiceGrpc.class) {
        if ((getGetOrderMethod = OrderServiceGrpc.getGetOrderMethod) == null) {
          OrderServiceGrpc.getGetOrderMethod = getGetOrderMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.order.v1.GetOrderRequest, com.fooddelivery.order.v1.GetOrderResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetOrder"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.order.v1.GetOrderRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.order.v1.GetOrderResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OrderServiceMethodDescriptorSupplier("GetOrder"))
              .build();
        }
      }
    }
    return getGetOrderMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.order.v1.ListCustomerOrdersRequest,
      com.fooddelivery.order.v1.ListCustomerOrdersResponse> getListCustomerOrdersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListCustomerOrders",
      requestType = com.fooddelivery.order.v1.ListCustomerOrdersRequest.class,
      responseType = com.fooddelivery.order.v1.ListCustomerOrdersResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.order.v1.ListCustomerOrdersRequest,
      com.fooddelivery.order.v1.ListCustomerOrdersResponse> getListCustomerOrdersMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.order.v1.ListCustomerOrdersRequest, com.fooddelivery.order.v1.ListCustomerOrdersResponse> getListCustomerOrdersMethod;
    if ((getListCustomerOrdersMethod = OrderServiceGrpc.getListCustomerOrdersMethod) == null) {
      synchronized (OrderServiceGrpc.class) {
        if ((getListCustomerOrdersMethod = OrderServiceGrpc.getListCustomerOrdersMethod) == null) {
          OrderServiceGrpc.getListCustomerOrdersMethod = getListCustomerOrdersMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.order.v1.ListCustomerOrdersRequest, com.fooddelivery.order.v1.ListCustomerOrdersResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListCustomerOrders"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.order.v1.ListCustomerOrdersRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.order.v1.ListCustomerOrdersResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OrderServiceMethodDescriptorSupplier("ListCustomerOrders"))
              .build();
        }
      }
    }
    return getListCustomerOrdersMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.order.v1.ListRestaurantOrdersRequest,
      com.fooddelivery.order.v1.ListRestaurantOrdersResponse> getListRestaurantOrdersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListRestaurantOrders",
      requestType = com.fooddelivery.order.v1.ListRestaurantOrdersRequest.class,
      responseType = com.fooddelivery.order.v1.ListRestaurantOrdersResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.order.v1.ListRestaurantOrdersRequest,
      com.fooddelivery.order.v1.ListRestaurantOrdersResponse> getListRestaurantOrdersMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.order.v1.ListRestaurantOrdersRequest, com.fooddelivery.order.v1.ListRestaurantOrdersResponse> getListRestaurantOrdersMethod;
    if ((getListRestaurantOrdersMethod = OrderServiceGrpc.getListRestaurantOrdersMethod) == null) {
      synchronized (OrderServiceGrpc.class) {
        if ((getListRestaurantOrdersMethod = OrderServiceGrpc.getListRestaurantOrdersMethod) == null) {
          OrderServiceGrpc.getListRestaurantOrdersMethod = getListRestaurantOrdersMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.order.v1.ListRestaurantOrdersRequest, com.fooddelivery.order.v1.ListRestaurantOrdersResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListRestaurantOrders"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.order.v1.ListRestaurantOrdersRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.order.v1.ListRestaurantOrdersResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OrderServiceMethodDescriptorSupplier("ListRestaurantOrders"))
              .build();
        }
      }
    }
    return getListRestaurantOrdersMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.order.v1.UpdateOrderStatusRequest,
      com.fooddelivery.order.v1.UpdateOrderStatusResponse> getUpdateOrderStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateOrderStatus",
      requestType = com.fooddelivery.order.v1.UpdateOrderStatusRequest.class,
      responseType = com.fooddelivery.order.v1.UpdateOrderStatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.order.v1.UpdateOrderStatusRequest,
      com.fooddelivery.order.v1.UpdateOrderStatusResponse> getUpdateOrderStatusMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.order.v1.UpdateOrderStatusRequest, com.fooddelivery.order.v1.UpdateOrderStatusResponse> getUpdateOrderStatusMethod;
    if ((getUpdateOrderStatusMethod = OrderServiceGrpc.getUpdateOrderStatusMethod) == null) {
      synchronized (OrderServiceGrpc.class) {
        if ((getUpdateOrderStatusMethod = OrderServiceGrpc.getUpdateOrderStatusMethod) == null) {
          OrderServiceGrpc.getUpdateOrderStatusMethod = getUpdateOrderStatusMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.order.v1.UpdateOrderStatusRequest, com.fooddelivery.order.v1.UpdateOrderStatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateOrderStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.order.v1.UpdateOrderStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.order.v1.UpdateOrderStatusResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OrderServiceMethodDescriptorSupplier("UpdateOrderStatus"))
              .build();
        }
      }
    }
    return getUpdateOrderStatusMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static OrderServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrderServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrderServiceStub>() {
        @java.lang.Override
        public OrderServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrderServiceStub(channel, callOptions);
        }
      };
    return OrderServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static OrderServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrderServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrderServiceBlockingV2Stub>() {
        @java.lang.Override
        public OrderServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrderServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return OrderServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static OrderServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrderServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrderServiceBlockingStub>() {
        @java.lang.Override
        public OrderServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrderServiceBlockingStub(channel, callOptions);
        }
      };
    return OrderServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static OrderServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrderServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrderServiceFutureStub>() {
        @java.lang.Override
        public OrderServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrderServiceFutureStub(channel, callOptions);
        }
      };
    return OrderServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createOrder(com.fooddelivery.order.v1.CreateOrderRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.order.v1.CreateOrderResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateOrderMethod(), responseObserver);
    }

    /**
     */
    default void getOrder(com.fooddelivery.order.v1.GetOrderRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.order.v1.GetOrderResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetOrderMethod(), responseObserver);
    }

    /**
     */
    default void listCustomerOrders(com.fooddelivery.order.v1.ListCustomerOrdersRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.order.v1.ListCustomerOrdersResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListCustomerOrdersMethod(), responseObserver);
    }

    /**
     */
    default void listRestaurantOrders(com.fooddelivery.order.v1.ListRestaurantOrdersRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.order.v1.ListRestaurantOrdersResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListRestaurantOrdersMethod(), responseObserver);
    }

    /**
     */
    default void updateOrderStatus(com.fooddelivery.order.v1.UpdateOrderStatusRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.order.v1.UpdateOrderStatusResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateOrderStatusMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service OrderService.
   */
  public static abstract class OrderServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return OrderServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service OrderService.
   */
  public static final class OrderServiceStub
      extends io.grpc.stub.AbstractAsyncStub<OrderServiceStub> {
    private OrderServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrderServiceStub(channel, callOptions);
    }

    /**
     */
    public void createOrder(com.fooddelivery.order.v1.CreateOrderRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.order.v1.CreateOrderResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateOrderMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getOrder(com.fooddelivery.order.v1.GetOrderRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.order.v1.GetOrderResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetOrderMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listCustomerOrders(com.fooddelivery.order.v1.ListCustomerOrdersRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.order.v1.ListCustomerOrdersResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListCustomerOrdersMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listRestaurantOrders(com.fooddelivery.order.v1.ListRestaurantOrdersRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.order.v1.ListRestaurantOrdersResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListRestaurantOrdersMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateOrderStatus(com.fooddelivery.order.v1.UpdateOrderStatusRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.order.v1.UpdateOrderStatusResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateOrderStatusMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service OrderService.
   */
  public static final class OrderServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<OrderServiceBlockingV2Stub> {
    private OrderServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrderServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public com.fooddelivery.order.v1.CreateOrderResponse createOrder(com.fooddelivery.order.v1.CreateOrderRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCreateOrderMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.order.v1.GetOrderResponse getOrder(com.fooddelivery.order.v1.GetOrderRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetOrderMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.order.v1.ListCustomerOrdersResponse listCustomerOrders(com.fooddelivery.order.v1.ListCustomerOrdersRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getListCustomerOrdersMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.order.v1.ListRestaurantOrdersResponse listRestaurantOrders(com.fooddelivery.order.v1.ListRestaurantOrdersRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getListRestaurantOrdersMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.order.v1.UpdateOrderStatusResponse updateOrderStatus(com.fooddelivery.order.v1.UpdateOrderStatusRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getUpdateOrderStatusMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service OrderService.
   */
  public static final class OrderServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<OrderServiceBlockingStub> {
    private OrderServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrderServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.fooddelivery.order.v1.CreateOrderResponse createOrder(com.fooddelivery.order.v1.CreateOrderRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateOrderMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.order.v1.GetOrderResponse getOrder(com.fooddelivery.order.v1.GetOrderRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetOrderMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.order.v1.ListCustomerOrdersResponse listCustomerOrders(com.fooddelivery.order.v1.ListCustomerOrdersRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListCustomerOrdersMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.order.v1.ListRestaurantOrdersResponse listRestaurantOrders(com.fooddelivery.order.v1.ListRestaurantOrdersRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListRestaurantOrdersMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.order.v1.UpdateOrderStatusResponse updateOrderStatus(com.fooddelivery.order.v1.UpdateOrderStatusRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateOrderStatusMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service OrderService.
   */
  public static final class OrderServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<OrderServiceFutureStub> {
    private OrderServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrderServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.order.v1.CreateOrderResponse> createOrder(
        com.fooddelivery.order.v1.CreateOrderRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateOrderMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.order.v1.GetOrderResponse> getOrder(
        com.fooddelivery.order.v1.GetOrderRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetOrderMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.order.v1.ListCustomerOrdersResponse> listCustomerOrders(
        com.fooddelivery.order.v1.ListCustomerOrdersRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListCustomerOrdersMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.order.v1.ListRestaurantOrdersResponse> listRestaurantOrders(
        com.fooddelivery.order.v1.ListRestaurantOrdersRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListRestaurantOrdersMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.order.v1.UpdateOrderStatusResponse> updateOrderStatus(
        com.fooddelivery.order.v1.UpdateOrderStatusRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateOrderStatusMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_ORDER = 0;
  private static final int METHODID_GET_ORDER = 1;
  private static final int METHODID_LIST_CUSTOMER_ORDERS = 2;
  private static final int METHODID_LIST_RESTAURANT_ORDERS = 3;
  private static final int METHODID_UPDATE_ORDER_STATUS = 4;

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
        case METHODID_CREATE_ORDER:
          serviceImpl.createOrder((com.fooddelivery.order.v1.CreateOrderRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.order.v1.CreateOrderResponse>) responseObserver);
          break;
        case METHODID_GET_ORDER:
          serviceImpl.getOrder((com.fooddelivery.order.v1.GetOrderRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.order.v1.GetOrderResponse>) responseObserver);
          break;
        case METHODID_LIST_CUSTOMER_ORDERS:
          serviceImpl.listCustomerOrders((com.fooddelivery.order.v1.ListCustomerOrdersRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.order.v1.ListCustomerOrdersResponse>) responseObserver);
          break;
        case METHODID_LIST_RESTAURANT_ORDERS:
          serviceImpl.listRestaurantOrders((com.fooddelivery.order.v1.ListRestaurantOrdersRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.order.v1.ListRestaurantOrdersResponse>) responseObserver);
          break;
        case METHODID_UPDATE_ORDER_STATUS:
          serviceImpl.updateOrderStatus((com.fooddelivery.order.v1.UpdateOrderStatusRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.order.v1.UpdateOrderStatusResponse>) responseObserver);
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
          getCreateOrderMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.order.v1.CreateOrderRequest,
              com.fooddelivery.order.v1.CreateOrderResponse>(
                service, METHODID_CREATE_ORDER)))
        .addMethod(
          getGetOrderMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.order.v1.GetOrderRequest,
              com.fooddelivery.order.v1.GetOrderResponse>(
                service, METHODID_GET_ORDER)))
        .addMethod(
          getListCustomerOrdersMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.order.v1.ListCustomerOrdersRequest,
              com.fooddelivery.order.v1.ListCustomerOrdersResponse>(
                service, METHODID_LIST_CUSTOMER_ORDERS)))
        .addMethod(
          getListRestaurantOrdersMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.order.v1.ListRestaurantOrdersRequest,
              com.fooddelivery.order.v1.ListRestaurantOrdersResponse>(
                service, METHODID_LIST_RESTAURANT_ORDERS)))
        .addMethod(
          getUpdateOrderStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.order.v1.UpdateOrderStatusRequest,
              com.fooddelivery.order.v1.UpdateOrderStatusResponse>(
                service, METHODID_UPDATE_ORDER_STATUS)))
        .build();
  }

  private static abstract class OrderServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    OrderServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.fooddelivery.order.v1.OrderOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("OrderService");
    }
  }

  private static final class OrderServiceFileDescriptorSupplier
      extends OrderServiceBaseDescriptorSupplier {
    OrderServiceFileDescriptorSupplier() {}
  }

  private static final class OrderServiceMethodDescriptorSupplier
      extends OrderServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    OrderServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (OrderServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new OrderServiceFileDescriptorSupplier())
              .addMethod(getCreateOrderMethod())
              .addMethod(getGetOrderMethod())
              .addMethod(getListCustomerOrdersMethod())
              .addMethod(getListRestaurantOrdersMethod())
              .addMethod(getUpdateOrderStatusMethod())
              .build();
        }
      }
    }
    return result;
  }
}
