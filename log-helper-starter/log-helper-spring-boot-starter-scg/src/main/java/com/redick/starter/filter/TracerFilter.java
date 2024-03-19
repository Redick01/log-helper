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

package com.redick.starter.filter;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.redick.constant.TraceTagConstant.*;

/**
 * @author Redick01
 */
@Slf4j
public class TracerFilter extends AbstractInterceptor implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        return chain.filter(exchange.mutate()
                .request(builder -> builder.headers(httpHeaders -> {
                    List<String> traceList = httpHeaders.get(Tracer.TRACE_ID);
                    String traceId = null;
                    if (traceList != null && !traceList.isEmpty()) {
                        traceId = traceList.get(0);
                    }
                    List<String> spanList = httpHeaders.get(Tracer.SPAN_ID);
                    String spanId = null;
                    if (spanList != null && !spanList.isEmpty()) {
                        spanId = spanList.get(0);
                    }
                    List<String> parentList = httpHeaders.get(Tracer.PARENT_ID);
                    String parentId = null;
                    if (parentList != null && !parentList.isEmpty()) {
                        parentId = parentList.get(0);
                    }
                    mdc(traceId, spanId, parentId);
                    httpHeaders.set(Tracer.TRACE_ID, traceId());
                    httpHeaders.set(Tracer.PARENT_ID, parentId());
                    httpHeaders.set(Tracer.SPAN_ID, spanId());
                    executeBefore(SCG_INVOKE_START + request.getURI().getPath());
                })).build())
                .transformDeferred(req -> {
                    final long start = System.currentTimeMillis();
                    return req.doFinally(s -> {
                        MDC.put(START_TIME, String.valueOf(start));
                        executeAfter(SCG_INVOKE_END);
                        MDC.clear();
                    });
                });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
