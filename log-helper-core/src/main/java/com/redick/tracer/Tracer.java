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

package com.redick.tracer;

import com.alibaba.ttl.TransmittableThreadLocal;
import java.util.UUID;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;

/**
 * @author Redick01
 */
@Data
public final class Tracer {

    public static final String TRACE_ID = "traceId";

    public static final String SPAN_ID = "spanId";

    public static final String PARENT_ID = "parentId";

    public static final String SKYWALKING_NO_ID = "[Ignored Trace]";

    public static TransmittableThreadLocal<Tracer> traceThreadLocal = new TransmittableThreadLocal<>();

    private String traceId;

    private Integer spanId;

    private Integer parentId;

    public Tracer(TracerBuilder builder) {
        this.traceId = builder.traceId;
        this.spanId = builder.spanId;
        this.parentId = builder.parentId;
        traceThreadLocal.set(this);
    }

    public static void trace(String traceId, String spanId, String parentId) {
        Tracer tracer = Tracer.traceThreadLocal.get();
        if (null == tracer) {
            tracer = new TracerBuilder()
                    .traceId(traceId)
                    .spanId(null == spanId ? null : Integer.parseInt(spanId))
                    .parentId(null == parentId ? null : Integer.parseInt(parentId))
                    .build();
            tracer.buildSpan();
        } else {
            traceThreadLocal.remove();
        }
    }

    @Trace
    public void buildSpan() {
        if (null == traceId) {
            if (StringUtils.isNotBlank(TraceContext.traceId()) && !SKYWALKING_NO_ID.equals(
                    TraceContext.traceId())) {
                traceId = TraceContext.traceId();
            } else {
                traceId = UUID.randomUUID().toString();
            }
        }
        parentId = null == parentId ? 0 : parentId + 1;
        spanId = null == spanId ? 1 : spanId + 1;
        MDC.put(TRACE_ID, traceId);
        MDC.put(SPAN_ID, spanId + "");
        MDC.put(PARENT_ID, parentId + "");
    }

    public static class TracerBuilder {

        private String traceId;

        private Integer spanId;

        private Integer parentId;

        public TracerBuilder traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        public TracerBuilder spanId(Integer spanId) {
            this.spanId = spanId;
            return this;
        }

        public TracerBuilder parentId(Integer parentId) {
            this.parentId = parentId;
            return this;
        }

        public Tracer build() {
            return new Tracer(this);
        }
    }
}
