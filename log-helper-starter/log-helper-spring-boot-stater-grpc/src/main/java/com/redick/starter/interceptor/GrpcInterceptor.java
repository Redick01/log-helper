package com.redick.starter.interceptor;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

/**
 * @author Redick01
 */
@Slf4j
@GrpcGlobalClientInterceptor
@GrpcGlobalServerInterceptor
public class GrpcInterceptor extends AbstractInterceptor implements ServerInterceptor, ClientInterceptor {

    private static final Metadata.Key<String> TRACE = Metadata.Key.of("traceId", Metadata.ASCII_STRING_MARSHALLER);

    private static final Metadata.Key<String> SPAN = Metadata.Key.of("spanId", Metadata.ASCII_STRING_MARSHALLER);

    private static final Metadata.Key<String> PARENT = Metadata.Key.of("parentId", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions,
            Channel channel) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String serviceName = methodDescriptor.getServiceName();
        String methodName = methodDescriptor.getFullMethodName();
        try {
            return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {
                @Override
                public void start(Listener<RespT> responseListener, Metadata headers) {
                    String traceId = traceId();
                    if (StringUtils.isNotBlank(traceId)) {
                        headers.put(TRACE, traceId);
                        headers.put(SPAN, spanId());
                        headers.put(PARENT, parentId());
                        log.info(LogUtil.marker(), "调用服务[{}]的方法[{}]", serviceName, methodName);
                    }
                    // 继续下一步
                    super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                        @Override
                        public void onHeaders(Metadata headers) {
                            // 服务端传递回来的header
                            super.onHeaders(headers);
                        }
                    }, headers);
                }
            };
        } finally {
            stopWatch.stop();
            log.info(LogUtil.marker(), "结束服务[{}]中方法[{}]的调用,耗时为:[{}]毫秒", serviceName, methodName, stopWatch.getTime());
        }
    }

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
            Metadata headers, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String serviceName = serverCall.getMethodDescriptor().getServiceName();
        String methodName = serverCall.getMethodDescriptor().getFullMethodName();
        log.info(LogUtil.marker(), "调用服务[{}]的方法[{}]", serviceName, methodName);
        String traceId = headers.get(TRACE);
        String spanId = headers.get(SPAN);
        String parentId = headers.get(PARENT);
        Tracer.trace(traceId, spanId, parentId);
        return serverCallHandler.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(serverCall) {

            @Override
            public void sendHeaders(Metadata responseHeaders) {
                super.sendHeaders(responseHeaders);
            }

            @Override
            public void close(Status status, Metadata trailers) {
                super.close(status, trailers);
            }
        }, headers);
    }
}
