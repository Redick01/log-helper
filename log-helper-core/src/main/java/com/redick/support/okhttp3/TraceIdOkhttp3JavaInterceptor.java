package com.redick.support.okhttp3;

import static com.redick.constant.TraceTagConstant.OKHTTP_CLIENT_EXEC_AFTER;
import static com.redick.constant.TraceTagConstant.OKHTTP_CLIENT_EXEC_BEFORE;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

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
                        .addHeader(Tracer.TRACE_ID, traceId)
                        .addHeader(Tracer.SPAN_ID, spanId())
                        .addHeader(Tracer.PARENT_ID, parentId())
                        .build();
            } else {
                log.info(LogUtil.marker(), "current thread have not the traceId!");
            }
        } catch (Exception e) {
            log.error(LogUtil.exceptionMarker(), "Okhttp3 http header set traceId exception!", e);
        }
        super.executeBefore(OKHTTP_CLIENT_EXEC_BEFORE);
        Response response = chain.proceed(request);
        super.executeBefore(OKHTTP_CLIENT_EXEC_AFTER);
        return response;
    }
}
