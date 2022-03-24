package com.redick.resetemplate;

import com.redick.common.GlobalSessionIdDefine;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
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

    @NotNull
    @Override
    public ClientHttpResponse intercept(HttpRequest request, @NotNull byte[] body, @NotNull ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders httpHeaders = request.getHeaders();
        String traceId = MDC.get(GlobalSessionIdDefine.GLOBAL_SESSION_ID_KEY);
        try {
            if (StringUtils.isNotBlank(traceId)) {
                httpHeaders.add(GlobalSessionIdDefine.GLOBAL_SESSION_ID_KEY, traceId);
            } else {
                log.info(LogUtil.marker(), "当前线程没有traceId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execution.execute(request, body);
    }
}
