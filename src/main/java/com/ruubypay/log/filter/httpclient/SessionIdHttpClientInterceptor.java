package com.ruubypay.log.filter.httpclient;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.apache.skywalking.apm.toolkit.trace.Trace;

import java.io.IOException;

/**
 * @author liupenghui
 * @date 2021/12/12 3:04 下午
 */
public class SessionIdHttpClient4Interceptor implements HttpRequestInterceptor {

    @Trace
    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {

    }
}
