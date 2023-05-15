package com.redick.starter.interceptor;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
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
        log.info(LogUtil.marker(message.getPayload()), "Received message");
        if (StringUtils.isNotBlank((String) message.getHeaders().get(Tracer.TRACE_ID))) {
            Tracer.trace((String) message.getHeaders().get(Tracer.TRACE_ID),
                    (String) message.getHeaders().get(Tracer.SPAN_ID),
                    (String) message.getHeaders().get(Tracer.PARENT_ID));
        }
        return ChannelInterceptor.super.preSend(message, channel);
    }

    @Override
    public void afterSendCompletion(@NotNull Message<?> message, @NotNull MessageChannel channel, boolean sent,
            Exception ex ) {
        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
        MDC.clear();
    }
}
