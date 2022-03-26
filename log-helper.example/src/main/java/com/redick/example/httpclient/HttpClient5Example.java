package com.redick.example.httpclient;


import com.redick.support.httpclient.TraceIdIdHttpClient5Interceptor;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpEntity;

import java.io.IOException;

/**
 * @author liupenghui
 * @date 2021/12/12 10:02 下午
 */
public class HttpClient5Example {

    public static void main(String[] args) {
        String url = "http://127.0.0.1:8081/order/getPayCount?orderNo=1";
        CloseableHttpClient client = HttpClientBuilder.create()
                .addRequestInterceptorFirst(new TraceIdIdHttpClient5Interceptor())
                .build();
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpResponse response = client.execute(get);
            HttpEntity entity  = response.getEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
