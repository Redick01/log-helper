package com.ruubypay.log.filter.httpclient;

import com.ruubypay.log.common.GlobalSessionIdDefine;
import com.ruubypay.log.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * @author liupenghui
 * @date 2021/12/12 3:04 下午
 */
@Slf4j
public class SessionIdHttpClientInterceptor implements HttpRequestInterceptor {

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        String globalSessionId = MDC.get(GlobalSessionIdDefine.kGLOBAL_SESSION_ID_KEY);
        try {
            if (StringUtils.isNotBlank(globalSessionId)) {
                // 传递traceId
                request.setHeader(GlobalSessionIdDefine.kGLOBAL_SESSION_ID_KEY, globalSessionId);
            } else {
                log.info(LogUtil.marker(), "当前线程没有x-global-session-id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
