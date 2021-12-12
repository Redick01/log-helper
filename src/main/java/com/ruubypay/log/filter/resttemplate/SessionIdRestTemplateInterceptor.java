package com.ruubypay.log.filter.resttemplate;

import com.ruubypay.log.common.GlobalSessionIdDefine;
import com.ruubypay.log.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author liupenghui
 * @date 2021/12/12 3:32 下午
 */
@Slf4j
public class SessionIdRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders httpHeaders = request.getHeaders();
        String traceId = MDC.get(GlobalSessionIdDefine.kGLOBAL_SESSION_ID_KEY);
        try {
            if (StringUtils.isNotBlank(traceId)) {
                httpHeaders.add(GlobalSessionIdDefine.kGLOBAL_SESSION_ID_KEY, traceId);
            } else {
                log.info(LogUtil.marker(), "当前线程没有x-global-session-id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execution.execute(request, body);
    }
}
