package com.github.egorkrylov.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.66.0)",
    comments = "Source: room_analytics.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class RoomAnalyticsGrpc {

  private RoomAnalyticsGrpc() {}

  public static final java.lang.String SERVICE_NAME = "roomanalytics.RoomAnalytics";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.github.egorkrylov.grpc.AnalyzeRoomRequest,
      com.github.egorkrylov.grpc.AnalyzeRoomResponse> getAnalyzeRoomMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AnalyzeRoom",
      requestType = com.github.egorkrylov.grpc.AnalyzeRoomRequest.class,
      responseType = com.github.egorkrylov.grpc.AnalyzeRoomResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.github.egorkrylov.grpc.AnalyzeRoomRequest,
      com.github.egorkrylov.grpc.AnalyzeRoomResponse> getAnalyzeRoomMethod() {
    io.grpc.MethodDescriptor<com.github.egorkrylov.grpc.AnalyzeRoomRequest, com.github.egorkrylov.grpc.AnalyzeRoomResponse> getAnalyzeRoomMethod;
    if ((getAnalyzeRoomMethod = RoomAnalyticsGrpc.getAnalyzeRoomMethod) == null) {
      synchronized (RoomAnalyticsGrpc.class) {
        if ((getAnalyzeRoomMethod = RoomAnalyticsGrpc.getAnalyzeRoomMethod) == null) {
          RoomAnalyticsGrpc.getAnalyzeRoomMethod = getAnalyzeRoomMethod =
              io.grpc.MethodDescriptor.<com.github.egorkrylov.grpc.AnalyzeRoomRequest, com.github.egorkrylov.grpc.AnalyzeRoomResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AnalyzeRoom"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.github.egorkrylov.grpc.AnalyzeRoomRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.github.egorkrylov.grpc.AnalyzeRoomResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RoomAnalyticsMethodDescriptorSupplier("AnalyzeRoom"))
              .build();
        }
      }
    }
    return getAnalyzeRoomMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RoomAnalyticsStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RoomAnalyticsStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RoomAnalyticsStub>() {
        @java.lang.Override
        public RoomAnalyticsStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RoomAnalyticsStub(channel, callOptions);
        }
      };
    return RoomAnalyticsStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RoomAnalyticsBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RoomAnalyticsBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RoomAnalyticsBlockingStub>() {
        @java.lang.Override
        public RoomAnalyticsBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RoomAnalyticsBlockingStub(channel, callOptions);
        }
      };
    return RoomAnalyticsBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RoomAnalyticsFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RoomAnalyticsFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RoomAnalyticsFutureStub>() {
        @java.lang.Override
        public RoomAnalyticsFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RoomAnalyticsFutureStub(channel, callOptions);
        }
      };
    return RoomAnalyticsFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void analyzeRoom(com.github.egorkrylov.grpc.AnalyzeRoomRequest request,
        io.grpc.stub.StreamObserver<com.github.egorkrylov.grpc.AnalyzeRoomResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAnalyzeRoomMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service RoomAnalytics.
   */
  public static abstract class RoomAnalyticsImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return RoomAnalyticsGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service RoomAnalytics.
   */
  public static final class RoomAnalyticsStub
      extends io.grpc.stub.AbstractAsyncStub<RoomAnalyticsStub> {
    private RoomAnalyticsStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RoomAnalyticsStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RoomAnalyticsStub(channel, callOptions);
    }

    /**
     */
    public void analyzeRoom(com.github.egorkrylov.grpc.AnalyzeRoomRequest request,
        io.grpc.stub.StreamObserver<com.github.egorkrylov.grpc.AnalyzeRoomResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAnalyzeRoomMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service RoomAnalytics.
   */
  public static final class RoomAnalyticsBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<RoomAnalyticsBlockingStub> {
    private RoomAnalyticsBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RoomAnalyticsBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RoomAnalyticsBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.github.egorkrylov.grpc.AnalyzeRoomResponse analyzeRoom(com.github.egorkrylov.grpc.AnalyzeRoomRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAnalyzeRoomMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service RoomAnalytics.
   */
  public static final class RoomAnalyticsFutureStub
      extends io.grpc.stub.AbstractFutureStub<RoomAnalyticsFutureStub> {
    private RoomAnalyticsFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RoomAnalyticsFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RoomAnalyticsFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.github.egorkrylov.grpc.AnalyzeRoomResponse> analyzeRoom(
        com.github.egorkrylov.grpc.AnalyzeRoomRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAnalyzeRoomMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ANALYZE_ROOM = 0;

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
        case METHODID_ANALYZE_ROOM:
          serviceImpl.analyzeRoom((com.github.egorkrylov.grpc.AnalyzeRoomRequest) request,
              (io.grpc.stub.StreamObserver<com.github.egorkrylov.grpc.AnalyzeRoomResponse>) responseObserver);
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
          getAnalyzeRoomMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.github.egorkrylov.grpc.AnalyzeRoomRequest,
              com.github.egorkrylov.grpc.AnalyzeRoomResponse>(
                service, METHODID_ANALYZE_ROOM)))
        .build();
  }

  private static abstract class RoomAnalyticsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RoomAnalyticsBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.github.egorkrylov.grpc.RoomAnalyticsOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RoomAnalytics");
    }
  }

  private static final class RoomAnalyticsFileDescriptorSupplier
      extends RoomAnalyticsBaseDescriptorSupplier {
    RoomAnalyticsFileDescriptorSupplier() {}
  }

  private static final class RoomAnalyticsMethodDescriptorSupplier
      extends RoomAnalyticsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    RoomAnalyticsMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (RoomAnalyticsGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RoomAnalyticsFileDescriptorSupplier())
              .addMethod(getAnalyzeRoomMethod())
              .build();
        }
      }
    }
    return result;
  }
}
