package com.redick.starter.filter;

import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author Redick01
 */
@Slf4j
public enum LogHelperTraceIdExchange {
    /**
     * 实例
     */
    INSTANCE;

    public ServerWebExchange preHandle(ServerWebExchange exchange) {
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        try {
            String traceId = Objects.requireNonNull(httpHeaders.get(Tracer.TRACE_ID)).get(0);
            String spanId = Objects.requireNonNull(httpHeaders.get(Tracer.SPAN_ID)).get(0);
            String parentId = Objects.requireNonNull(httpHeaders.get(Tracer.PARENT_ID)).get(0);
            Tracer.trace(traceId, spanId, parentId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(LogUtil.exceptionMarker(), "spring webflux tracer filter exception.", e);
        }

        return exchange;
    }
}
