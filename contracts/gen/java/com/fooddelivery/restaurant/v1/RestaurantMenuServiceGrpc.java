package com.fooddelivery.restaurant.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Restaurant operations
 * </pre>
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class RestaurantMenuServiceGrpc {

  private RestaurantMenuServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "fooddelivery.restaurant.v1.RestaurantMenuService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.CreateRestaurantRequest,
      com.fooddelivery.restaurant.v1.CreateRestaurantResponse> getCreateRestaurantMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateRestaurant",
      requestType = com.fooddelivery.restaurant.v1.CreateRestaurantRequest.class,
      responseType = com.fooddelivery.restaurant.v1.CreateRestaurantResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.CreateRestaurantRequest,
      com.fooddelivery.restaurant.v1.CreateRestaurantResponse> getCreateRestaurantMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.CreateRestaurantRequest, com.fooddelivery.restaurant.v1.CreateRestaurantResponse> getCreateRestaurantMethod;
    if ((getCreateRestaurantMethod = RestaurantMenuServiceGrpc.getCreateRestaurantMethod) == null) {
      synchronized (RestaurantMenuServiceGrpc.class) {
        if ((getCreateRestaurantMethod = RestaurantMenuServiceGrpc.getCreateRestaurantMethod) == null) {
          RestaurantMenuServiceGrpc.getCreateRestaurantMethod = getCreateRestaurantMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.restaurant.v1.CreateRestaurantRequest, com.fooddelivery.restaurant.v1.CreateRestaurantResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateRestaurant"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.CreateRestaurantRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.CreateRestaurantResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RestaurantMenuServiceMethodDescriptorSupplier("CreateRestaurant"))
              .build();
        }
      }
    }
    return getCreateRestaurantMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.GetRestaurantRequest,
      com.fooddelivery.restaurant.v1.GetRestaurantResponse> getGetRestaurantMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetRestaurant",
      requestType = com.fooddelivery.restaurant.v1.GetRestaurantRequest.class,
      responseType = com.fooddelivery.restaurant.v1.GetRestaurantResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.GetRestaurantRequest,
      com.fooddelivery.restaurant.v1.GetRestaurantResponse> getGetRestaurantMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.GetRestaurantRequest, com.fooddelivery.restaurant.v1.GetRestaurantResponse> getGetRestaurantMethod;
    if ((getGetRestaurantMethod = RestaurantMenuServiceGrpc.getGetRestaurantMethod) == null) {
      synchronized (RestaurantMenuServiceGrpc.class) {
        if ((getGetRestaurantMethod = RestaurantMenuServiceGrpc.getGetRestaurantMethod) == null) {
          RestaurantMenuServiceGrpc.getGetRestaurantMethod = getGetRestaurantMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.restaurant.v1.GetRestaurantRequest, com.fooddelivery.restaurant.v1.GetRestaurantResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetRestaurant"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.GetRestaurantRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.GetRestaurantResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RestaurantMenuServiceMethodDescriptorSupplier("GetRestaurant"))
              .build();
        }
      }
    }
    return getGetRestaurantMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.ListRestaurantsRequest,
      com.fooddelivery.restaurant.v1.ListRestaurantsResponse> getListRestaurantsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListRestaurants",
      requestType = com.fooddelivery.restaurant.v1.ListRestaurantsRequest.class,
      responseType = com.fooddelivery.restaurant.v1.ListRestaurantsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.ListRestaurantsRequest,
      com.fooddelivery.restaurant.v1.ListRestaurantsResponse> getListRestaurantsMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.ListRestaurantsRequest, com.fooddelivery.restaurant.v1.ListRestaurantsResponse> getListRestaurantsMethod;
    if ((getListRestaurantsMethod = RestaurantMenuServiceGrpc.getListRestaurantsMethod) == null) {
      synchronized (RestaurantMenuServiceGrpc.class) {
        if ((getListRestaurantsMethod = RestaurantMenuServiceGrpc.getListRestaurantsMethod) == null) {
          RestaurantMenuServiceGrpc.getListRestaurantsMethod = getListRestaurantsMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.restaurant.v1.ListRestaurantsRequest, com.fooddelivery.restaurant.v1.ListRestaurantsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListRestaurants"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.ListRestaurantsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.ListRestaurantsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RestaurantMenuServiceMethodDescriptorSupplier("ListRestaurants"))
              .build();
        }
      }
    }
    return getListRestaurantsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.ListOwnedRestaurantsRequest,
      com.fooddelivery.restaurant.v1.ListOwnedRestaurantsResponse> getListOwnedRestaurantsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListOwnedRestaurants",
      requestType = com.fooddelivery.restaurant.v1.ListOwnedRestaurantsRequest.class,
      responseType = com.fooddelivery.restaurant.v1.ListOwnedRestaurantsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.ListOwnedRestaurantsRequest,
      com.fooddelivery.restaurant.v1.ListOwnedRestaurantsResponse> getListOwnedRestaurantsMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.ListOwnedRestaurantsRequest, com.fooddelivery.restaurant.v1.ListOwnedRestaurantsResponse> getListOwnedRestaurantsMethod;
    if ((getListOwnedRestaurantsMethod = RestaurantMenuServiceGrpc.getListOwnedRestaurantsMethod) == null) {
      synchronized (RestaurantMenuServiceGrpc.class) {
        if ((getListOwnedRestaurantsMethod = RestaurantMenuServiceGrpc.getListOwnedRestaurantsMethod) == null) {
          RestaurantMenuServiceGrpc.getListOwnedRestaurantsMethod = getListOwnedRestaurantsMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.restaurant.v1.ListOwnedRestaurantsRequest, com.fooddelivery.restaurant.v1.ListOwnedRestaurantsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListOwnedRestaurants"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.ListOwnedRestaurantsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.ListOwnedRestaurantsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RestaurantMenuServiceMethodDescriptorSupplier("ListOwnedRestaurants"))
              .build();
        }
      }
    }
    return getListOwnedRestaurantsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.UpdateRestaurantRequest,
      com.fooddelivery.restaurant.v1.UpdateRestaurantResponse> getUpdateRestaurantMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateRestaurant",
      requestType = com.fooddelivery.restaurant.v1.UpdateRestaurantRequest.class,
      responseType = com.fooddelivery.restaurant.v1.UpdateRestaurantResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.UpdateRestaurantRequest,
      com.fooddelivery.restaurant.v1.UpdateRestaurantResponse> getUpdateRestaurantMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.UpdateRestaurantRequest, com.fooddelivery.restaurant.v1.UpdateRestaurantResponse> getUpdateRestaurantMethod;
    if ((getUpdateRestaurantMethod = RestaurantMenuServiceGrpc.getUpdateRestaurantMethod) == null) {
      synchronized (RestaurantMenuServiceGrpc.class) {
        if ((getUpdateRestaurantMethod = RestaurantMenuServiceGrpc.getUpdateRestaurantMethod) == null) {
          RestaurantMenuServiceGrpc.getUpdateRestaurantMethod = getUpdateRestaurantMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.restaurant.v1.UpdateRestaurantRequest, com.fooddelivery.restaurant.v1.UpdateRestaurantResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateRestaurant"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.UpdateRestaurantRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.UpdateRestaurantResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RestaurantMenuServiceMethodDescriptorSupplier("UpdateRestaurant"))
              .build();
        }
      }
    }
    return getUpdateRestaurantMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.DeleteRestaurantRequest,
      com.fooddelivery.restaurant.v1.DeleteRestaurantResponse> getDeleteRestaurantMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteRestaurant",
      requestType = com.fooddelivery.restaurant.v1.DeleteRestaurantRequest.class,
      responseType = com.fooddelivery.restaurant.v1.DeleteRestaurantResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.DeleteRestaurantRequest,
      com.fooddelivery.restaurant.v1.DeleteRestaurantResponse> getDeleteRestaurantMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.DeleteRestaurantRequest, com.fooddelivery.restaurant.v1.DeleteRestaurantResponse> getDeleteRestaurantMethod;
    if ((getDeleteRestaurantMethod = RestaurantMenuServiceGrpc.getDeleteRestaurantMethod) == null) {
      synchronized (RestaurantMenuServiceGrpc.class) {
        if ((getDeleteRestaurantMethod = RestaurantMenuServiceGrpc.getDeleteRestaurantMethod) == null) {
          RestaurantMenuServiceGrpc.getDeleteRestaurantMethod = getDeleteRestaurantMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.restaurant.v1.DeleteRestaurantRequest, com.fooddelivery.restaurant.v1.DeleteRestaurantResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteRestaurant"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.DeleteRestaurantRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.DeleteRestaurantResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RestaurantMenuServiceMethodDescriptorSupplier("DeleteRestaurant"))
              .build();
        }
      }
    }
    return getDeleteRestaurantMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.GetMenuRequest,
      com.fooddelivery.restaurant.v1.GetMenuResponse> getGetMenuMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetMenu",
      requestType = com.fooddelivery.restaurant.v1.GetMenuRequest.class,
      responseType = com.fooddelivery.restaurant.v1.GetMenuResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.GetMenuRequest,
      com.fooddelivery.restaurant.v1.GetMenuResponse> getGetMenuMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.GetMenuRequest, com.fooddelivery.restaurant.v1.GetMenuResponse> getGetMenuMethod;
    if ((getGetMenuMethod = RestaurantMenuServiceGrpc.getGetMenuMethod) == null) {
      synchronized (RestaurantMenuServiceGrpc.class) {
        if ((getGetMenuMethod = RestaurantMenuServiceGrpc.getGetMenuMethod) == null) {
          RestaurantMenuServiceGrpc.getGetMenuMethod = getGetMenuMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.restaurant.v1.GetMenuRequest, com.fooddelivery.restaurant.v1.GetMenuResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetMenu"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.GetMenuRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.GetMenuResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RestaurantMenuServiceMethodDescriptorSupplier("GetMenu"))
              .build();
        }
      }
    }
    return getGetMenuMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.CreateMenuItemRequest,
      com.fooddelivery.restaurant.v1.CreateMenuItemResponse> getCreateMenuItemMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateMenuItem",
      requestType = com.fooddelivery.restaurant.v1.CreateMenuItemRequest.class,
      responseType = com.fooddelivery.restaurant.v1.CreateMenuItemResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.CreateMenuItemRequest,
      com.fooddelivery.restaurant.v1.CreateMenuItemResponse> getCreateMenuItemMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.CreateMenuItemRequest, com.fooddelivery.restaurant.v1.CreateMenuItemResponse> getCreateMenuItemMethod;
    if ((getCreateMenuItemMethod = RestaurantMenuServiceGrpc.getCreateMenuItemMethod) == null) {
      synchronized (RestaurantMenuServiceGrpc.class) {
        if ((getCreateMenuItemMethod = RestaurantMenuServiceGrpc.getCreateMenuItemMethod) == null) {
          RestaurantMenuServiceGrpc.getCreateMenuItemMethod = getCreateMenuItemMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.restaurant.v1.CreateMenuItemRequest, com.fooddelivery.restaurant.v1.CreateMenuItemResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateMenuItem"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.CreateMenuItemRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.CreateMenuItemResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RestaurantMenuServiceMethodDescriptorSupplier("CreateMenuItem"))
              .build();
        }
      }
    }
    return getCreateMenuItemMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.UpdateMenuItemRequest,
      com.fooddelivery.restaurant.v1.UpdateMenuItemResponse> getUpdateMenuItemMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateMenuItem",
      requestType = com.fooddelivery.restaurant.v1.UpdateMenuItemRequest.class,
      responseType = com.fooddelivery.restaurant.v1.UpdateMenuItemResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.UpdateMenuItemRequest,
      com.fooddelivery.restaurant.v1.UpdateMenuItemResponse> getUpdateMenuItemMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.UpdateMenuItemRequest, com.fooddelivery.restaurant.v1.UpdateMenuItemResponse> getUpdateMenuItemMethod;
    if ((getUpdateMenuItemMethod = RestaurantMenuServiceGrpc.getUpdateMenuItemMethod) == null) {
      synchronized (RestaurantMenuServiceGrpc.class) {
        if ((getUpdateMenuItemMethod = RestaurantMenuServiceGrpc.getUpdateMenuItemMethod) == null) {
          RestaurantMenuServiceGrpc.getUpdateMenuItemMethod = getUpdateMenuItemMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.restaurant.v1.UpdateMenuItemRequest, com.fooddelivery.restaurant.v1.UpdateMenuItemResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateMenuItem"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.UpdateMenuItemRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.UpdateMenuItemResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RestaurantMenuServiceMethodDescriptorSupplier("UpdateMenuItem"))
              .build();
        }
      }
    }
    return getUpdateMenuItemMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.DeleteMenuItemRequest,
      com.fooddelivery.restaurant.v1.DeleteMenuItemResponse> getDeleteMenuItemMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteMenuItem",
      requestType = com.fooddelivery.restaurant.v1.DeleteMenuItemRequest.class,
      responseType = com.fooddelivery.restaurant.v1.DeleteMenuItemResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.DeleteMenuItemRequest,
      com.fooddelivery.restaurant.v1.DeleteMenuItemResponse> getDeleteMenuItemMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.DeleteMenuItemRequest, com.fooddelivery.restaurant.v1.DeleteMenuItemResponse> getDeleteMenuItemMethod;
    if ((getDeleteMenuItemMethod = RestaurantMenuServiceGrpc.getDeleteMenuItemMethod) == null) {
      synchronized (RestaurantMenuServiceGrpc.class) {
        if ((getDeleteMenuItemMethod = RestaurantMenuServiceGrpc.getDeleteMenuItemMethod) == null) {
          RestaurantMenuServiceGrpc.getDeleteMenuItemMethod = getDeleteMenuItemMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.restaurant.v1.DeleteMenuItemRequest, com.fooddelivery.restaurant.v1.DeleteMenuItemResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteMenuItem"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.DeleteMenuItemRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.DeleteMenuItemResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RestaurantMenuServiceMethodDescriptorSupplier("DeleteMenuItem"))
              .build();
        }
      }
    }
    return getDeleteMenuItemMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.QuoteOrderRequest,
      com.fooddelivery.restaurant.v1.QuoteOrderResponse> getQuoteOrderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "QuoteOrder",
      requestType = com.fooddelivery.restaurant.v1.QuoteOrderRequest.class,
      responseType = com.fooddelivery.restaurant.v1.QuoteOrderResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.QuoteOrderRequest,
      com.fooddelivery.restaurant.v1.QuoteOrderResponse> getQuoteOrderMethod() {
    io.grpc.MethodDescriptor<com.fooddelivery.restaurant.v1.QuoteOrderRequest, com.fooddelivery.restaurant.v1.QuoteOrderResponse> getQuoteOrderMethod;
    if ((getQuoteOrderMethod = RestaurantMenuServiceGrpc.getQuoteOrderMethod) == null) {
      synchronized (RestaurantMenuServiceGrpc.class) {
        if ((getQuoteOrderMethod = RestaurantMenuServiceGrpc.getQuoteOrderMethod) == null) {
          RestaurantMenuServiceGrpc.getQuoteOrderMethod = getQuoteOrderMethod =
              io.grpc.MethodDescriptor.<com.fooddelivery.restaurant.v1.QuoteOrderRequest, com.fooddelivery.restaurant.v1.QuoteOrderResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "QuoteOrder"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.QuoteOrderRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fooddelivery.restaurant.v1.QuoteOrderResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RestaurantMenuServiceMethodDescriptorSupplier("QuoteOrder"))
              .build();
        }
      }
    }
    return getQuoteOrderMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RestaurantMenuServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RestaurantMenuServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RestaurantMenuServiceStub>() {
        @java.lang.Override
        public RestaurantMenuServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RestaurantMenuServiceStub(channel, callOptions);
        }
      };
    return RestaurantMenuServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static RestaurantMenuServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RestaurantMenuServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RestaurantMenuServiceBlockingV2Stub>() {
        @java.lang.Override
        public RestaurantMenuServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RestaurantMenuServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return RestaurantMenuServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RestaurantMenuServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RestaurantMenuServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RestaurantMenuServiceBlockingStub>() {
        @java.lang.Override
        public RestaurantMenuServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RestaurantMenuServiceBlockingStub(channel, callOptions);
        }
      };
    return RestaurantMenuServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RestaurantMenuServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RestaurantMenuServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RestaurantMenuServiceFutureStub>() {
        @java.lang.Override
        public RestaurantMenuServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RestaurantMenuServiceFutureStub(channel, callOptions);
        }
      };
    return RestaurantMenuServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Restaurant operations
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void createRestaurant(com.fooddelivery.restaurant.v1.CreateRestaurantRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.CreateRestaurantResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateRestaurantMethod(), responseObserver);
    }

    /**
     */
    default void getRestaurant(com.fooddelivery.restaurant.v1.GetRestaurantRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.GetRestaurantResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetRestaurantMethod(), responseObserver);
    }

    /**
     */
    default void listRestaurants(com.fooddelivery.restaurant.v1.ListRestaurantsRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.ListRestaurantsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListRestaurantsMethod(), responseObserver);
    }

    /**
     */
    default void listOwnedRestaurants(com.fooddelivery.restaurant.v1.ListOwnedRestaurantsRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.ListOwnedRestaurantsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListOwnedRestaurantsMethod(), responseObserver);
    }

    /**
     */
    default void updateRestaurant(com.fooddelivery.restaurant.v1.UpdateRestaurantRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.UpdateRestaurantResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateRestaurantMethod(), responseObserver);
    }

    /**
     */
    default void deleteRestaurant(com.fooddelivery.restaurant.v1.DeleteRestaurantRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.DeleteRestaurantResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteRestaurantMethod(), responseObserver);
    }

    /**
     */
    default void getMenu(com.fooddelivery.restaurant.v1.GetMenuRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.GetMenuResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMenuMethod(), responseObserver);
    }

    /**
     */
    default void createMenuItem(com.fooddelivery.restaurant.v1.CreateMenuItemRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.CreateMenuItemResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateMenuItemMethod(), responseObserver);
    }

    /**
     */
    default void updateMenuItem(com.fooddelivery.restaurant.v1.UpdateMenuItemRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.UpdateMenuItemResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateMenuItemMethod(), responseObserver);
    }

    /**
     */
    default void deleteMenuItem(com.fooddelivery.restaurant.v1.DeleteMenuItemRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.DeleteMenuItemResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteMenuItemMethod(), responseObserver);
    }

    /**
     * <pre>
     * Internal gRPC operation. No public HTTP binding.
     * </pre>
     */
    default void quoteOrder(com.fooddelivery.restaurant.v1.QuoteOrderRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.QuoteOrderResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getQuoteOrderMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service RestaurantMenuService.
   * <pre>
   * Restaurant operations
   * </pre>
   */
  public static abstract class RestaurantMenuServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return RestaurantMenuServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service RestaurantMenuService.
   * <pre>
   * Restaurant operations
   * </pre>
   */
  public static final class RestaurantMenuServiceStub
      extends io.grpc.stub.AbstractAsyncStub<RestaurantMenuServiceStub> {
    private RestaurantMenuServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RestaurantMenuServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RestaurantMenuServiceStub(channel, callOptions);
    }

    /**
     */
    public void createRestaurant(com.fooddelivery.restaurant.v1.CreateRestaurantRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.CreateRestaurantResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateRestaurantMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getRestaurant(com.fooddelivery.restaurant.v1.GetRestaurantRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.GetRestaurantResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetRestaurantMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listRestaurants(com.fooddelivery.restaurant.v1.ListRestaurantsRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.ListRestaurantsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListRestaurantsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listOwnedRestaurants(com.fooddelivery.restaurant.v1.ListOwnedRestaurantsRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.ListOwnedRestaurantsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListOwnedRestaurantsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateRestaurant(com.fooddelivery.restaurant.v1.UpdateRestaurantRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.UpdateRestaurantResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateRestaurantMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteRestaurant(com.fooddelivery.restaurant.v1.DeleteRestaurantRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.DeleteRestaurantResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteRestaurantMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getMenu(com.fooddelivery.restaurant.v1.GetMenuRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.GetMenuResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetMenuMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createMenuItem(com.fooddelivery.restaurant.v1.CreateMenuItemRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.CreateMenuItemResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateMenuItemMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateMenuItem(com.fooddelivery.restaurant.v1.UpdateMenuItemRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.UpdateMenuItemResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateMenuItemMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteMenuItem(com.fooddelivery.restaurant.v1.DeleteMenuItemRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.DeleteMenuItemResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteMenuItemMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Internal gRPC operation. No public HTTP binding.
     * </pre>
     */
    public void quoteOrder(com.fooddelivery.restaurant.v1.QuoteOrderRequest request,
        io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.QuoteOrderResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getQuoteOrderMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service RestaurantMenuService.
   * <pre>
   * Restaurant operations
   * </pre>
   */
  public static final class RestaurantMenuServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<RestaurantMenuServiceBlockingV2Stub> {
    private RestaurantMenuServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RestaurantMenuServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RestaurantMenuServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.CreateRestaurantResponse createRestaurant(com.fooddelivery.restaurant.v1.CreateRestaurantRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCreateRestaurantMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.GetRestaurantResponse getRestaurant(com.fooddelivery.restaurant.v1.GetRestaurantRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetRestaurantMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.ListRestaurantsResponse listRestaurants(com.fooddelivery.restaurant.v1.ListRestaurantsRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getListRestaurantsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.ListOwnedRestaurantsResponse listOwnedRestaurants(com.fooddelivery.restaurant.v1.ListOwnedRestaurantsRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getListOwnedRestaurantsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.UpdateRestaurantResponse updateRestaurant(com.fooddelivery.restaurant.v1.UpdateRestaurantRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getUpdateRestaurantMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.DeleteRestaurantResponse deleteRestaurant(com.fooddelivery.restaurant.v1.DeleteRestaurantRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getDeleteRestaurantMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.GetMenuResponse getMenu(com.fooddelivery.restaurant.v1.GetMenuRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetMenuMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.CreateMenuItemResponse createMenuItem(com.fooddelivery.restaurant.v1.CreateMenuItemRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCreateMenuItemMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.UpdateMenuItemResponse updateMenuItem(com.fooddelivery.restaurant.v1.UpdateMenuItemRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getUpdateMenuItemMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.DeleteMenuItemResponse deleteMenuItem(com.fooddelivery.restaurant.v1.DeleteMenuItemRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getDeleteMenuItemMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Internal gRPC operation. No public HTTP binding.
     * </pre>
     */
    public com.fooddelivery.restaurant.v1.QuoteOrderResponse quoteOrder(com.fooddelivery.restaurant.v1.QuoteOrderRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getQuoteOrderMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service RestaurantMenuService.
   * <pre>
   * Restaurant operations
   * </pre>
   */
  public static final class RestaurantMenuServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<RestaurantMenuServiceBlockingStub> {
    private RestaurantMenuServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RestaurantMenuServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RestaurantMenuServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.CreateRestaurantResponse createRestaurant(com.fooddelivery.restaurant.v1.CreateRestaurantRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateRestaurantMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.GetRestaurantResponse getRestaurant(com.fooddelivery.restaurant.v1.GetRestaurantRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetRestaurantMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.ListRestaurantsResponse listRestaurants(com.fooddelivery.restaurant.v1.ListRestaurantsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListRestaurantsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.ListOwnedRestaurantsResponse listOwnedRestaurants(com.fooddelivery.restaurant.v1.ListOwnedRestaurantsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListOwnedRestaurantsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.UpdateRestaurantResponse updateRestaurant(com.fooddelivery.restaurant.v1.UpdateRestaurantRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateRestaurantMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.DeleteRestaurantResponse deleteRestaurant(com.fooddelivery.restaurant.v1.DeleteRestaurantRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteRestaurantMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.GetMenuResponse getMenu(com.fooddelivery.restaurant.v1.GetMenuRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetMenuMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.CreateMenuItemResponse createMenuItem(com.fooddelivery.restaurant.v1.CreateMenuItemRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateMenuItemMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.UpdateMenuItemResponse updateMenuItem(com.fooddelivery.restaurant.v1.UpdateMenuItemRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateMenuItemMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.fooddelivery.restaurant.v1.DeleteMenuItemResponse deleteMenuItem(com.fooddelivery.restaurant.v1.DeleteMenuItemRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteMenuItemMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Internal gRPC operation. No public HTTP binding.
     * </pre>
     */
    public com.fooddelivery.restaurant.v1.QuoteOrderResponse quoteOrder(com.fooddelivery.restaurant.v1.QuoteOrderRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getQuoteOrderMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service RestaurantMenuService.
   * <pre>
   * Restaurant operations
   * </pre>
   */
  public static final class RestaurantMenuServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<RestaurantMenuServiceFutureStub> {
    private RestaurantMenuServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RestaurantMenuServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RestaurantMenuServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.restaurant.v1.CreateRestaurantResponse> createRestaurant(
        com.fooddelivery.restaurant.v1.CreateRestaurantRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateRestaurantMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.restaurant.v1.GetRestaurantResponse> getRestaurant(
        com.fooddelivery.restaurant.v1.GetRestaurantRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetRestaurantMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.restaurant.v1.ListRestaurantsResponse> listRestaurants(
        com.fooddelivery.restaurant.v1.ListRestaurantsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListRestaurantsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.restaurant.v1.ListOwnedRestaurantsResponse> listOwnedRestaurants(
        com.fooddelivery.restaurant.v1.ListOwnedRestaurantsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListOwnedRestaurantsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.restaurant.v1.UpdateRestaurantResponse> updateRestaurant(
        com.fooddelivery.restaurant.v1.UpdateRestaurantRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateRestaurantMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.restaurant.v1.DeleteRestaurantResponse> deleteRestaurant(
        com.fooddelivery.restaurant.v1.DeleteRestaurantRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteRestaurantMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.restaurant.v1.GetMenuResponse> getMenu(
        com.fooddelivery.restaurant.v1.GetMenuRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetMenuMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.restaurant.v1.CreateMenuItemResponse> createMenuItem(
        com.fooddelivery.restaurant.v1.CreateMenuItemRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateMenuItemMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.restaurant.v1.UpdateMenuItemResponse> updateMenuItem(
        com.fooddelivery.restaurant.v1.UpdateMenuItemRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateMenuItemMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.restaurant.v1.DeleteMenuItemResponse> deleteMenuItem(
        com.fooddelivery.restaurant.v1.DeleteMenuItemRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteMenuItemMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Internal gRPC operation. No public HTTP binding.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fooddelivery.restaurant.v1.QuoteOrderResponse> quoteOrder(
        com.fooddelivery.restaurant.v1.QuoteOrderRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getQuoteOrderMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_RESTAURANT = 0;
  private static final int METHODID_GET_RESTAURANT = 1;
  private static final int METHODID_LIST_RESTAURANTS = 2;
  private static final int METHODID_LIST_OWNED_RESTAURANTS = 3;
  private static final int METHODID_UPDATE_RESTAURANT = 4;
  private static final int METHODID_DELETE_RESTAURANT = 5;
  private static final int METHODID_GET_MENU = 6;
  private static final int METHODID_CREATE_MENU_ITEM = 7;
  private static final int METHODID_UPDATE_MENU_ITEM = 8;
  private static final int METHODID_DELETE_MENU_ITEM = 9;
  private static final int METHODID_QUOTE_ORDER = 10;

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
        case METHODID_CREATE_RESTAURANT:
          serviceImpl.createRestaurant((com.fooddelivery.restaurant.v1.CreateRestaurantRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.CreateRestaurantResponse>) responseObserver);
          break;
        case METHODID_GET_RESTAURANT:
          serviceImpl.getRestaurant((com.fooddelivery.restaurant.v1.GetRestaurantRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.GetRestaurantResponse>) responseObserver);
          break;
        case METHODID_LIST_RESTAURANTS:
          serviceImpl.listRestaurants((com.fooddelivery.restaurant.v1.ListRestaurantsRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.ListRestaurantsResponse>) responseObserver);
          break;
        case METHODID_LIST_OWNED_RESTAURANTS:
          serviceImpl.listOwnedRestaurants((com.fooddelivery.restaurant.v1.ListOwnedRestaurantsRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.ListOwnedRestaurantsResponse>) responseObserver);
          break;
        case METHODID_UPDATE_RESTAURANT:
          serviceImpl.updateRestaurant((com.fooddelivery.restaurant.v1.UpdateRestaurantRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.UpdateRestaurantResponse>) responseObserver);
          break;
        case METHODID_DELETE_RESTAURANT:
          serviceImpl.deleteRestaurant((com.fooddelivery.restaurant.v1.DeleteRestaurantRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.DeleteRestaurantResponse>) responseObserver);
          break;
        case METHODID_GET_MENU:
          serviceImpl.getMenu((com.fooddelivery.restaurant.v1.GetMenuRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.GetMenuResponse>) responseObserver);
          break;
        case METHODID_CREATE_MENU_ITEM:
          serviceImpl.createMenuItem((com.fooddelivery.restaurant.v1.CreateMenuItemRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.CreateMenuItemResponse>) responseObserver);
          break;
        case METHODID_UPDATE_MENU_ITEM:
          serviceImpl.updateMenuItem((com.fooddelivery.restaurant.v1.UpdateMenuItemRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.UpdateMenuItemResponse>) responseObserver);
          break;
        case METHODID_DELETE_MENU_ITEM:
          serviceImpl.deleteMenuItem((com.fooddelivery.restaurant.v1.DeleteMenuItemRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.DeleteMenuItemResponse>) responseObserver);
          break;
        case METHODID_QUOTE_ORDER:
          serviceImpl.quoteOrder((com.fooddelivery.restaurant.v1.QuoteOrderRequest) request,
              (io.grpc.stub.StreamObserver<com.fooddelivery.restaurant.v1.QuoteOrderResponse>) responseObserver);
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
          getCreateRestaurantMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.restaurant.v1.CreateRestaurantRequest,
              com.fooddelivery.restaurant.v1.CreateRestaurantResponse>(
                service, METHODID_CREATE_RESTAURANT)))
        .addMethod(
          getGetRestaurantMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.restaurant.v1.GetRestaurantRequest,
              com.fooddelivery.restaurant.v1.GetRestaurantResponse>(
                service, METHODID_GET_RESTAURANT)))
        .addMethod(
          getListRestaurantsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.restaurant.v1.ListRestaurantsRequest,
              com.fooddelivery.restaurant.v1.ListRestaurantsResponse>(
                service, METHODID_LIST_RESTAURANTS)))
        .addMethod(
          getListOwnedRestaurantsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.restaurant.v1.ListOwnedRestaurantsRequest,
              com.fooddelivery.restaurant.v1.ListOwnedRestaurantsResponse>(
                service, METHODID_LIST_OWNED_RESTAURANTS)))
        .addMethod(
          getUpdateRestaurantMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.restaurant.v1.UpdateRestaurantRequest,
              com.fooddelivery.restaurant.v1.UpdateRestaurantResponse>(
                service, METHODID_UPDATE_RESTAURANT)))
        .addMethod(
          getDeleteRestaurantMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.restaurant.v1.DeleteRestaurantRequest,
              com.fooddelivery.restaurant.v1.DeleteRestaurantResponse>(
                service, METHODID_DELETE_RESTAURANT)))
        .addMethod(
          getGetMenuMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.restaurant.v1.GetMenuRequest,
              com.fooddelivery.restaurant.v1.GetMenuResponse>(
                service, METHODID_GET_MENU)))
        .addMethod(
          getCreateMenuItemMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.restaurant.v1.CreateMenuItemRequest,
              com.fooddelivery.restaurant.v1.CreateMenuItemResponse>(
                service, METHODID_CREATE_MENU_ITEM)))
        .addMethod(
          getUpdateMenuItemMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.restaurant.v1.UpdateMenuItemRequest,
              com.fooddelivery.restaurant.v1.UpdateMenuItemResponse>(
                service, METHODID_UPDATE_MENU_ITEM)))
        .addMethod(
          getDeleteMenuItemMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.restaurant.v1.DeleteMenuItemRequest,
              com.fooddelivery.restaurant.v1.DeleteMenuItemResponse>(
                service, METHODID_DELETE_MENU_ITEM)))
        .addMethod(
          getQuoteOrderMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.fooddelivery.restaurant.v1.QuoteOrderRequest,
              com.fooddelivery.restaurant.v1.QuoteOrderResponse>(
                service, METHODID_QUOTE_ORDER)))
        .build();
  }

  private static abstract class RestaurantMenuServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RestaurantMenuServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.fooddelivery.restaurant.v1.RestaurantOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RestaurantMenuService");
    }
  }

  private static final class RestaurantMenuServiceFileDescriptorSupplier
      extends RestaurantMenuServiceBaseDescriptorSupplier {
    RestaurantMenuServiceFileDescriptorSupplier() {}
  }

  private static final class RestaurantMenuServiceMethodDescriptorSupplier
      extends RestaurantMenuServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    RestaurantMenuServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (RestaurantMenuServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RestaurantMenuServiceFileDescriptorSupplier())
              .addMethod(getCreateRestaurantMethod())
              .addMethod(getGetRestaurantMethod())
              .addMethod(getListRestaurantsMethod())
              .addMethod(getListOwnedRestaurantsMethod())
              .addMethod(getUpdateRestaurantMethod())
              .addMethod(getDeleteRestaurantMethod())
              .addMethod(getGetMenuMethod())
              .addMethod(getCreateMenuItemMethod())
              .addMethod(getUpdateMenuItemMethod())
              .addMethod(getDeleteMenuItemMethod())
              .addMethod(getQuoteOrderMethod())
              .build();
        }
      }
    }
    return result;
  }
}
