package com.redick.support.resttemplate;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author liupenghui
 *  2021/12/12 3:32 下午
 */
@Slf4j
public class TraceIdRestTemplateInterceptor extends AbstractInterceptor implements ClientHttpRequestInterceptor {

    @NotNull
    @Override
    public ClientHttpResponse intercept(HttpRequest request, @NotNull byte[] body, @NotNull ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders httpHeaders = request.getHeaders();
        String traceId = traceId();
        try {
            if (StringUtils.isNotBlank(traceId)) {
                httpHeaders.add(Tracer.TRACE_ID, traceId);
                httpHeaders.add(Tracer.SPAN_ID, spanId());
                httpHeaders.add(Tracer.PARENT_ID, parentId());
            } else {
                log.info(LogUtil.marker(), "current thread have not the traceId!");
            }
        } catch (Exception e) {
            log.error(LogUtil.exceptionMarker(), "RestTemplate http header set traceId exception!", e);
        }
        return execution.execute(request, body);
    }
}
