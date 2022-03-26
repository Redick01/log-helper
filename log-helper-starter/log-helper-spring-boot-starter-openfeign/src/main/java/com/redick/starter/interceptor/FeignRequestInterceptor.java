package com.redick.starter.interceptor;

import com.redick.util.LogUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * @author Redick01
 * @date 2021/12/18 10:05 下午
 */
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            String traceId = MDC.get(LogUtil.kLOG_KEY_TRACE_ID);
            if (StringUtils.isNotBlank(traceId)) {
                requestTemplate.header(LogUtil.kLOG_KEY_TRACE_ID, traceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(LogUtil.exceptionMarker(), "openfeign log filter exception.", e);
        }
    }
}
