package com.ruubypay.log.filter.okhttp;

import com.ruubypay.log.common.GlobalSessionIdDefine;
import com.ruubypay.log.util.LogUtil;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * @author liupenghui
 * @date 2021/12/12 3:13 下午
 */
@Slf4j
public class SessionIdOkhttpInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        try {
            String traceId = MDC.get(GlobalSessionIdDefine.kGLOBAL_SESSION_ID_KEY);
            if (StringUtils.isNotBlank(traceId)) {
                request = request
                        .newBuilder()
                        .addHeader(GlobalSessionIdDefine.kGLOBAL_SESSION_ID_KEY, traceId)
                        .build();
            } else {
                log.info(LogUtil.marker(), "当前线程没有x-global-session-id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chain.proceed(request);
    }
}
