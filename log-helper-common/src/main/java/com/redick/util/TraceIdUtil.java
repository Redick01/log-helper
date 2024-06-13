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

package com.redick.util;

import com.redick.constant.TraceIdDefine;
import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;

import java.util.UUID;

/**
 * @author: Redick01
 * @date: 2024/6/13 17:14
 */
public class TraceIdUtil {

    @Trace
    public static String traceId() {
        // skywalking traceId
        String swId = "";
        if (StringUtils.isNotBlank(TraceContext.traceId()) && !TraceIdDefine.SKYWALKING_NO_ID.equals(
                TraceContext.traceId())) {
            swId = TraceContext.traceId();
        }
        // jaeger traceId
        String jgId = "";
        Span activeSpan = GlobalTracer.get().activeSpan();
        if (null != activeSpan) {
            jgId = activeSpan.context().toTraceId();
        }
        // skywalking traceId 优先级最高，其次jaeger，最次随机数
        if (StringUtils.isNotBlank(swId)) {
            return swId;
        }
        if (StringUtils.isNotBlank(jgId)) {
            return jgId;
        }
        return UUID.randomUUID().toString();
    }
}
