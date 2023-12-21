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

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

/**
 * @author Redick01
 */
@Slf4j
public class InputChannelTraceInterceptor extends AbstractInterceptor implements
        ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, @NotNull MessageChannel channel) {
        if (StringUtils.isNotBlank((String) message.getHeaders().get(Tracer.TRACE_ID))) {
            Tracer.trace((String) message.getHeaders().get(Tracer.TRACE_ID),
                    (String) message.getHeaders().get(Tracer.SPAN_ID),
                    (String) message.getHeaders().get(Tracer.PARENT_ID));
        }
        return ChannelInterceptor.super.preSend(message, channel);
    }

    @Override
    public void afterSendCompletion(@NotNull Message<?> message, @NotNull MessageChannel channel, boolean sent,
                                    Exception ex) {
        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
        MDC.clear();
    }
}
