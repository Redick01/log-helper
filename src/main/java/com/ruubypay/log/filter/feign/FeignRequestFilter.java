package com.ruubypay.log.filter.feign;

import com.ruubypay.log.util.LogUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * @author liupenghui
 * @date 2021/12/3 12:29 上午
 */
@Slf4j
public class FeignRequestFilter implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            String sessionId = MDC.get(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY);
            if (StringUtils.isNotBlank(sessionId)) {
                requestTemplate.header(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY, sessionId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(LogUtil.exceptionMarker(), "openfeign log filter exception.", e);
        }
    }
}
