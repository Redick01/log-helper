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

package com.redick.support.resttemplate;

import static com.redick.constant.TraceTagConstant.REST_TEMPLATE_EXEC_AFTER;
import static com.redick.constant.TraceTagConstant.REST_TEMPLATE_EXEC_BEFORE;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author liupenghui
 *  2021/12/12 3:32 下午
 */
@Slf4j
public class TraceIdRestTemplateInterceptor extends AbstractInterceptor implements ClientHttpRequestInterceptor {

    @NotNull
    @Override
    public ClientHttpResponse intercept(HttpRequest request, @NotNull byte[] body, @NotNull ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders httpHeaders = request.getHeaders();
        String traceId = traceId();
        try {
            if (StringUtils.isNotBlank(traceId)) {
                httpHeaders.add(Tracer.TRACE_ID, traceId);
                httpHeaders.add(Tracer.SPAN_ID, spanId());
                httpHeaders.add(Tracer.PARENT_ID, parentId());
            } else {
                log.info(LogUtil.marker(), "current thread have not the traceId!");
            }
        } catch (Exception e) {
            log.error(LogUtil.exceptionMarker(), "RestTemplate http header set traceId exception!", e);
        }
        super.executeBefore(REST_TEMPLATE_EXEC_BEFORE);
        var response = execution.execute(request, body);
        super.executeAfter(REST_TEMPLATE_EXEC_AFTER);
        return response;
    }
}
