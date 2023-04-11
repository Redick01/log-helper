package com.redick.support.httpclient;

import static com.redick.constant.TraceTagConstant.HTTP_CLIENT_EXEC_BEFORE;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * @author liupenghui
 *  2021/12/12 3:04 下午
 */
@Slf4j
public class TraceIdHttpRequestInterceptor extends AbstractInterceptor implements HttpRequestInterceptor {

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        try {
            if (StringUtils.isNotBlank(traceId())) {
                // 传递traceId
                request.setHeader(Tracer.TRACE_ID, traceId());
                request.setHeader(Tracer.SPAN_ID, spanId());
                request.setHeader(Tracer.PARENT_ID, parentId());
                super.executeBefore(HTTP_CLIENT_EXEC_BEFORE);
            } else {
                log.info(LogUtil.marker(), "current thread have not the traceId!");
            }
        } catch (Exception e) {
            log.error(LogUtil.exceptionMarker(), "HttpClient http header set traceId exception!", e);
        }
    }
}
