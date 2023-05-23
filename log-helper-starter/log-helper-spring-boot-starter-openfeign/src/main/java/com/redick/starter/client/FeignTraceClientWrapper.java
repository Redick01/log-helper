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

package com.redick.starter.client;

import static com.redick.constant.TraceTagConstant.OPEN_FEIGN_INVOKE_AFTER;
import static com.redick.constant.TraceTagConstant.OPEN_FEIGN_INVOKE_BEFORE;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import feign.Client;
import feign.Request;
import feign.Request.Options;
import feign.Response;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Redick01
 */
public class FeignTraceClientWrapper extends AbstractInterceptor implements Client {

    private final Client delegate;

    public FeignTraceClientWrapper(Client delegate) {
        this.delegate = delegate;
    }

    @Override
    public Response execute(Request request, Options options) throws IOException {
        String traceId = traceId();
        if (StringUtils.isNotBlank(traceId)) {
            request = setRequestHeader(request);
            super.executeBefore(OPEN_FEIGN_INVOKE_BEFORE);
        }
        Response response = delegate.execute(request, options);
        executeAfter(OPEN_FEIGN_INVOKE_AFTER);
        return response;
    }

    private Request setRequestHeader(Request request) {
        Map<String, Collection<String>> headers = request.headers();
        Map<String, Collection<String>> newHeaders = new ConcurrentHashMap<>(16);
        newHeaders.putAll(headers);
        newHeaders.put(Tracer.TRACE_ID, Collections.singleton(traceId()));
        newHeaders.put(Tracer.PARENT_ID, Collections.singleton(parentId()));
        newHeaders.put(Tracer.SPAN_ID, Collections.singleton(spanId()));
        return Request.create(request.httpMethod(), request.url(), newHeaders, request.body(),
                request.charset(), request.requestTemplate());
    }
}
