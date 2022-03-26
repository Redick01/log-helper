package com.redick.support.okhttp;

import com.redick.common.TraceIdDefine;
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
 * @author liupenghui
 * @date 2021/12/12 3:22 下午
 */
@Slf4j
public class TraceIdOkhttp3Interceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        try {
            String traceId = MDC.get(TraceIdDefine.TRACE_ID);
            if (StringUtils.isNotBlank(traceId)) {
                request = request
                        .newBuilder()
                        .addHeader(TraceIdDefine.TRACE_ID, traceId)
                        .build();
            } else {
                log.info(LogUtil.marker(), "当前线程没有traceId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chain.proceed(request);
    }
}
