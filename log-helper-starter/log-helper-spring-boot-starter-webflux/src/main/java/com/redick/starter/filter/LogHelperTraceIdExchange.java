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

import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author Redick01
 */
@Slf4j
public enum LogHelperTraceIdExchange {
    /**
     * 实例
     */
    INSTANCE;

    public ServerWebExchange preHandle(ServerWebExchange exchange) {
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        try {
            String traceId = Objects.requireNonNull(httpHeaders.get(Tracer.TRACE_ID)).get(0);
            String spanId = Objects.requireNonNull(httpHeaders.get(Tracer.SPAN_ID)).get(0);
            String parentId = Objects.requireNonNull(httpHeaders.get(Tracer.PARENT_ID)).get(0);
            Tracer.trace(traceId, spanId, parentId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(LogUtil.exceptionMarker(), "spring webflux tracer filter exception.", e);
        }

        return exchange;
    }
}
