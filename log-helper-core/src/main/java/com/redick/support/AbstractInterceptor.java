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

package com.redick.support;

import static com.redick.constant.TraceTagConstant.START_TIME;
import static net.logstash.logback.marker.Markers.append;

import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author Redick01
 * 2022/3/24 21:01
 */
@Slf4j
public abstract class AbstractInterceptor {

    /**
     * get trace id.
     *
     * @return trace id
     */
    public String traceId() {
        return MDC.get(Tracer.TRACE_ID);
    }

    /**
     * get span id.
     *
     * @return span id
     */
    public String spanId() {
        return MDC.get(Tracer.SPAN_ID);
    }

    /**
     * get parent id.
     *
     * @return parent id
     */
    public String parentId() {
        return MDC.get(Tracer.PARENT_ID);
    }

    @Trace
    public void mdc(String traceId, String spanId, String parentId) {
        if (null == traceId) {
            if (StringUtils.isNotBlank(TraceContext.traceId()) && !Tracer.SKYWALKING_NO_ID.equals(
                    TraceContext.traceId())) {
                traceId = TraceContext.traceId();
            } else {
                traceId = UUID.randomUUID().toString();
            }
        }
        parentId = null == parentId ? "0" : Integer.parseInt(parentId) + 1 + "";
        spanId = null == spanId ? "1" : Integer.parseInt(spanId) + 1 + "";
        MDC.put(Tracer.TRACE_ID, traceId);
        MDC.put(Tracer.SPAN_ID, spanId);
        MDC.put(Tracer.PARENT_ID, parentId);
    }

    /**
     * before
     *
     * @param traceTag 标签
     */
    protected void executeBefore(final String traceTag) {
        MDC.put(START_TIME, String.valueOf(System.currentTimeMillis()));
        log.info(LogUtil.customizeMarker(LogUtil.kLOG_KEY_TRACE_TAG, traceTag), traceTag);
    }

    /**
     * after
     *
     * @param traceTag 标签
     */
    protected void executeAfter(final String traceTag) {
        String start = MDC.get(START_TIME);
        if (StringUtils.isNotBlank(start)) {
            long startTime = Long.parseLong(start);
            long duration = System.currentTimeMillis() - startTime;
            log.info(LogUtil.customizeMarker(LogUtil.kLOG_KEY_TRACE_TAG, traceTag)
                            .and(append(LogUtil.kLOG_KEY_DURATION, duration)), traceTag);
        }
    }
}
