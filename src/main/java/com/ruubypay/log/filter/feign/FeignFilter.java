package com.ruubypay.log.filter.feign;

import com.ruubypay.log.util.LogUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author liupenghui
 * @date 2021/12/3 12:29 上午
 */
@Slf4j
public class FeignFilter implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            String sessionId = MDC.get(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY);
            if (StringUtils.isNotBlank(sessionId)) {
                requestTemplate.header(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY, sessionId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(LogUtil.exceptionMarker(), "openfeign log filter exception.", e);
        } finally {
            stopWatch.stop();
            log.info(LogUtil.marker(), "调用URL:[{}]的调用,耗时为:[{}]毫秒", requestTemplate.url(),
                    stopWatch.getTime());
        }
    }
}
