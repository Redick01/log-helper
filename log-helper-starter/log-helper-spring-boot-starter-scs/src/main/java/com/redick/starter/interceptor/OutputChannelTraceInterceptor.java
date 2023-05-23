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

package com.redick.starter.interceptor;

import static com.redick.constant.TraceTagConstant.SCS_INVOKE_AFTER;
import static com.redick.constant.TraceTagConstant.SCS_INVOKE_BEFORE;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import java.lang.reflect.Field;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.util.ReflectionUtils;

/**
 * @author Redick01
 */
@Slf4j
public class OutputChannelTraceInterceptor extends AbstractInterceptor implements
        ChannelInterceptor {

    private final Field headersField;

    public OutputChannelTraceInterceptor() {
        this.headersField = ReflectionUtils.findField(MessageHeaders.class, "headers");
        assert this.headersField != null;
        this.headersField.setAccessible(true);
    }

    @Override
    public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
        Tracer.trace(traceId(), spanId(), parentId());
        Map<String, Object> headers = (Map<String, Object>) ReflectionUtils
                .getField(this.headersField, message.getHeaders());
        headers.put(Tracer.TRACE_ID, traceId());
        headers.put(Tracer.SPAN_ID, spanId());
        headers.put(Tracer.PARENT_ID, parentId());
        executeBefore(SCS_INVOKE_BEFORE);
        return ChannelInterceptor.super.preSend(message, channel);
    }

    @Override
    public void afterSendCompletion(@NotNull Message<?> message, @NotNull MessageChannel channel, boolean sent,
            Exception ex) {
        executeAfter(SCS_INVOKE_AFTER);
        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
        MDC.clear();
    }
}
