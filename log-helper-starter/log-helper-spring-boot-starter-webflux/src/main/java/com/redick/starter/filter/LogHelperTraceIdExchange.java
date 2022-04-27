package com.redick.starter.filter;

import com.redick.common.TraceIdDefine;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.UUID;

/**
 * @author Redick01
 */
@Slf4j
public enum LogHelperTraceIdExchange {
    /**
     * 实例
     */
    INSTANCE;

    @Trace
    public ServerWebExchange preHandle(ServerWebExchange exchange) {
        String traceId = null;
        List<String> traceIds = exchange.getRequest().getHeaders().get(TraceIdDefine.TRACE_ID);
        if (traceIds != null && traceIds.size() > 0) {
            traceId = traceIds.get(0);
        } else {
            if (StringUtils.isNotBlank(TraceContext.traceId())) {
                traceId = TraceContext.traceId();
            } else {
                traceId = UUID.randomUUID().toString();
            }
        }
        MDC.put(LogUtil.kLOG_KEY_TRACE_ID, traceId);
        return exchange;
    }
}
