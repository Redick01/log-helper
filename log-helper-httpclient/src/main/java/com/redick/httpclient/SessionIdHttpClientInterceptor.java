package com.redick.httpclient;

import com.redick.common.GlobalSessionIdDefine;
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
 * @date 2021/12/12 3:04 下午
 */
@Slf4j
public class SessionIdHttpClientInterceptor extends AbstractInterceptor implements HttpRequestInterceptor {

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        try {
            if (StringUtils.isNotBlank(traceId())) {
                // 传递traceId
                request.setHeader(GlobalSessionIdDefine.GLOBAL_SESSION_ID_KEY, traceId());
            } else {
                log.info(LogUtil.marker(), "当前线程没有traceId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
