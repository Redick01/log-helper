/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redick.starter.interceptor;

import static com.redick.constant.TraceTagConstant.GRPC_INVOKE_AFTER;
import static com.redick.constant.TraceTagConstant.GRPC_INVOKE_BEFORE;

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
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor,
                callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String traceId = traceId();
                if (StringUtils.isNotBlank(traceId)) {
                    executeBefore(GRPC_INVOKE_BEFORE);
                    headers.put(TRACE, traceId);
                    headers.put(SPAN, spanId());
                    headers.put(PARENT, parentId());
                }
                // 继续下一步
                super.start(new ForwardingClientCallListener
                        .SimpleForwardingClientCallListener<RespT>(responseListener) {
                    @Override
                    public void onHeaders(Metadata headers) {
                        // 服务端传递回来的header
                        super.onHeaders(headers);
                        executeAfter(GRPC_INVOKE_AFTER);
                    }
                }, headers);
            }
        };
    }

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
            Metadata headers, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String traceId = headers.get(TRACE);
        String spanId = headers.get(SPAN);
        String parentId = headers.get(PARENT);
        Tracer.trace(traceId, spanId, parentId);
        log.info(LogUtil.marker(), "开始处理");
        return serverCallHandler.startCall(new ForwardingServerCall
                .SimpleForwardingServerCall<ReqT, RespT>(serverCall) {

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
