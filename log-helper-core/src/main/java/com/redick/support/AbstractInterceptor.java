package com.redick.support;

import com.redick.tracer.Tracer;
import org.slf4j.MDC;

/**
 * @author Redick01
 *  2022/3/24 21:01
 */
public abstract class AbstractInterceptor {

    public String traceId() {
        return MDC.get(Tracer.TRACE_ID);
    }

    public String spanId() {
        return MDC.get(Tracer.SPAN_ID);
    }

    public String parentId() {
        return MDC.get(Tracer.PARENT_ID);
    }
}
