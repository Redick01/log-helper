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

package com.redick.support.httpclient5;

import static com.redick.constant.TraceTagConstant.HTTP_CLIENT_EXEC_BEFORE;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;

/**
 * @author liupenghui
 *  2021/12/12 3:10 下午
 */
@Slf4j
public class TraceIdHttpRequest5Interceptor extends AbstractInterceptor implements HttpRequestInterceptor {

    @Override
    public void process(HttpRequest request, EntityDetails entityDetails, HttpContext httpContext) throws HttpException, IOException {
        try {
            if (StringUtils.isNotBlank(traceId())) {
                // 传递traceId
                request.setHeader(Tracer.TRACE_ID, traceId());
                request.setHeader(Tracer.SPAN_ID, spanId());
                request.setHeader(Tracer.PARENT_ID, parentId());
                super.executeBefore(HTTP_CLIENT_EXEC_BEFORE);
            } else {
                log.info(LogUtil.marker(), "current thread have not the traceId!");
            }
        } catch (Exception e) {
            log.error(LogUtil.exceptionMarker(), "HttpClient5 http header set traceId exception!", e);
        }
    }
}
