package com.redick.support.hutoolhttpclient;

import cn.hutool.http.HttpInterceptor;
import cn.hutool.http.HttpRequest;
import com.redick.common.TraceIdDefine;
import com.redick.support.AbstractInterceptor;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Redick01
 * @date 2022/4/18 13:13
 */
@Slf4j
public class TraceIdHutoolHttpClientInterceptor extends AbstractInterceptor implements HttpInterceptor {

    @Override
    public void process(HttpRequest request) {
        try {
            if (StringUtils.isNotBlank(traceId())) {
                // 传递traceId
                Map<String, String> header = new HashMap<>();
                header.put(TraceIdDefine.TRACE_ID, traceId());
                request.addHeaders(header);
            } else {
                log.info(LogUtil.marker(), "current thread have not the traceId!");
            }
        } catch (Exception e) {
            log.error(LogUtil.exceptionMarker(), "HttpClient http header set traceId exception!", e);
        }
    }
}
