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

package com.redick.support.okhttp;

import static com.redick.constant.TraceTagConstant.OKHTTP_CLIENT_EXEC_AFTER;
import static com.redick.constant.TraceTagConstant.OKHTTP_CLIENT_EXEC_BEFORE;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * @author liupenghui
 *  2021/12/12 3:13 下午
 */
@Slf4j
public class TraceIdOkhttpInterceptor extends AbstractInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        try {
            String traceId = traceId();
            if (StringUtils.isNotBlank(traceId)) {
                request = request
                        .newBuilder()
                        .addHeader(Tracer.TRACE_ID, traceId)
                        .addHeader(Tracer.SPAN_ID, spanId())
                        .addHeader(Tracer.PARENT_ID, parentId())
                        .build();
            } else {
                log.info(LogUtil.marker(), "current thread have not the traceId!");
            }
        } catch (Exception e) {
            log.error(LogUtil.exceptionMarker(), "Okhttp http header set traceId exception!", e);
        }
        super.executeBefore(OKHTTP_CLIENT_EXEC_BEFORE);
        Response response = chain.proceed(request);
        super.executeAfter(OKHTTP_CLIENT_EXEC_AFTER);
        return response;
    }
}
