package com.redick.support.okhttp;

import static com.redick.constant.TraceTagConstant.OKHTTP_CLIENT_EXEC_AFTER;
import static com.redick.constant.TraceTagConstant.OKHTTP_CLIENT_EXEC_BEFORE;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
                        .addHeader(Tracer.TRACE_ID, traceId)
                        .addHeader(Tracer.SPAN_ID, spanId())
                        .addHeader(Tracer.PARENT_ID, parentId())
                        .build();
            } else {
                log.info(LogUtil.marker(), "current thread have not the traceId!");
            }
        } catch (Exception e) {
            log.error(LogUtil.exceptionMarker(), "Okhttp http header set traceId exception!", e);
        }
        super.executeBefore(OKHTTP_CLIENT_EXEC_BEFORE);
        Response response = chain.proceed(request);
        super.executeAfter(OKHTTP_CLIENT_EXEC_AFTER);
        return response;
    }
}
