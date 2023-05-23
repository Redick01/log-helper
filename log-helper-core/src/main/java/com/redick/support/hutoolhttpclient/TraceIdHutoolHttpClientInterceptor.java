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

package com.redick.support.hutoolhttpclient;

import cn.hutool.http.HttpInterceptor;
import cn.hutool.http.HttpRequest;
import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Redick01
 */
@Slf4j
public class TraceIdHutoolHttpClientInterceptor extends AbstractInterceptor implements HttpInterceptor {

    @Override
    public void process(HttpRequest request) {
        try {
            if (StringUtils.isNotBlank(traceId())) {
                // 传递traceId
                Map<String, String> header = new HashMap<>();
                header.put(Tracer.TRACE_ID, traceId());
                header.put(Tracer.SPAN_ID, spanId());
                header.put(Tracer.PARENT_ID, parentId());
                request.addHeaders(header);
            } else {
                log.info(LogUtil.marker(), "current thread have not the traceId!");
            }
        } catch (Exception e) {
            log.error(LogUtil.exceptionMarker(), "HttpClient http header set traceId exception!", e);
        }
    }
}
