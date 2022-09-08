package com.redick.starter.interceptor;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Redick01
 * 2021/12/18 10:05 下午
 */
@Slf4j
public class FeignRequestInterceptor extends AbstractInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            String traceId = traceId();
            if (StringUtils.isNotBlank(traceId)) {
                requestTemplate.header(Tracer.TRACE_ID, traceId);
                requestTemplate.header(Tracer.SPAN_ID, spanId());
                requestTemplate.header(Tracer.PARENT_ID, parentId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(LogUtil.exceptionMarker(), "openfeign log filter exception.", e);
        }
    }
}
