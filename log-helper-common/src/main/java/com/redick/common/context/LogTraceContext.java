package com.redick.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author Redick01
 * @date 2022/4/26 19:44
 */
public class LogTraceContext {

    private static final String TRACE_ID_KEY = "traceId";

    private static final String PRE_SERVICE = "preService";

    private static final String PRE_BUSINESS_DESCRIPTION = "preBusinessDescription";

    private static final String PRE_INSTANCE = "preInstance";

    private static final TransmittableThreadLocal<String> traceId = new TransmittableThreadLocal<>();

    private static final TransmittableThreadLocal<String> preService = new TransmittableThreadLocal<>();

    private static final TransmittableThreadLocal<String> preBusinessDescription = new TransmittableThreadLocal<>();

    private static final TransmittableThreadLocal<String> preInstance = new TransmittableThreadLocal<>();

    @Trace
    public void put(String preService, String instance) {
        String traceId = MDC.get(TRACE_ID_KEY);
        if (StringUtils.isBlank(traceId)) {
            traceId = StringUtils.isBlank(TraceContext.traceId()) ? UUID.randomUUID().toString() : TraceContext.traceId();
        }
        String preBusinessDescription = "";
    }
}
