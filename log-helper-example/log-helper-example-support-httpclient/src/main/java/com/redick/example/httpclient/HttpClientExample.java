package com.redick.example.httpclient;

import com.redick.support.httpclient.TraceIdHttpClientInterceptor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

/**
 * @author liupenghui
 * @date 2021/12/12 9:59 下午
 */
public class HttpClientExample {

    public static void main(String[] args) {
        String url = "http://127.0.0.1:8081/order/getPayCount?orderNo=1";
        CloseableHttpClient client = HttpClientBuilder.create()
                .addInterceptorFirst(new TraceIdHttpClientInterceptor())
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
