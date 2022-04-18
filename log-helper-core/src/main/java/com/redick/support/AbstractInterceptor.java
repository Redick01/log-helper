package com.redick.support;

import com.redick.common.TraceIdDefine;
import org.slf4j.MDC;

/**
 * @author Redick01
 *  2022/3/24 21:01
 */
public abstract class AbstractInterceptor {

    public String traceId() {
        return MDC.get(TraceIdDefine.TRACE_ID);
    }
}
