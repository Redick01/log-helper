package com.redick.support.okhttp;

import com.redick.common.TraceIdDefine;
import com.redick.support.AbstractInterceptor;
import com.redick.util.LogUtil;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * @author liupenghui
 *  2021/12/12 3:13 下午
 */
@Slf4j
public class TraceIdOkhttpInterceptor extends AbstractInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        try {
            String traceId = traceId();
            if (StringUtils.isNotBlank(traceId)) {
                request = request
                        .newBuilder()
                        .addHeader(TraceIdDefine.TRACE_ID, traceId)
                        .build();
            } else {
                log.info(LogUtil.marker(), "current thread have not the traceId!");
            }
        } catch (Exception e) {
            log.error(LogUtil.exceptionMarker(), "Okhttp http header set traceId exception!", e);
        }
        return chain.proceed(request);
    }
}
