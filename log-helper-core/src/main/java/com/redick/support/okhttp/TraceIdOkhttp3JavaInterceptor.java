package com.redick.support.okhttp;

import com.redick.common.TraceIdDefine;
import com.redick.support.AbstractInterceptor;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * @author Redick01
 */
@Slf4j
public class TraceIdOkhttp3JavaInterceptor extends AbstractInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
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
            log.error(LogUtil.exceptionMarker(), "Okhttp3 http header set traceId exception!", e);
        }
        return chain.proceed(request);
    }
}
