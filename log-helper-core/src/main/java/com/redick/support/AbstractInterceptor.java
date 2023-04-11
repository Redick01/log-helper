package com.redick.support;

import static com.redick.constant.TraceTagConstant.START_TIME;
import static net.logstash.logback.marker.Markers.append;

import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * @author Redick01
 *  2022/3/24 21:01
 */
@Slf4j
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

    protected void executeBefore(final String tranceTag) {
        MDC.put(START_TIME, String.valueOf(System.currentTimeMillis()));
        log.info(LogUtil.customizeMarker(LogUtil.kLOG_KEY_TRACE_TAG, tranceTag), tranceTag);
    }

    protected void executeAfter(final String tranceTag) {
        String start = MDC.get(START_TIME);
        if (StringUtils.isNotBlank(start)) {
            long startTime = Long.parseLong(start);
            long duration = System.currentTimeMillis() - startTime;
            log.info(LogUtil.customizeMarker(LogUtil.kLOG_KEY_TRACE_TAG, tranceTag)
                            .and(append(LogUtil.kLOG_KEY_DURATION, duration))
                    , tranceTag);
        }
    }
}
