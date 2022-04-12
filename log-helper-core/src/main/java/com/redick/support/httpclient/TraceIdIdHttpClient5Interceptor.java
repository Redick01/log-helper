package com.redick.support.httpclient;

import com.redick.common.TraceIdDefine;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;

/**
 * @author liupenghui
 *  2021/12/12 3:10 下午
 */
@Slf4j
public class TraceIdIdHttpClient5Interceptor extends AbstractInterceptor implements HttpRequestInterceptor {

    @Override
    public void process(HttpRequest httpRequest, EntityDetails entityDetails, HttpContext httpContext) throws HttpException, IOException {
        try {
            if (StringUtils.isNotBlank(traceId())) {
                // 传递traceId
                httpRequest.setHeader(TraceIdDefine.TRACE_ID, traceId());
            } else {
                log.info(LogUtil.marker(), "当前线程没有traceId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
