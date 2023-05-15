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
