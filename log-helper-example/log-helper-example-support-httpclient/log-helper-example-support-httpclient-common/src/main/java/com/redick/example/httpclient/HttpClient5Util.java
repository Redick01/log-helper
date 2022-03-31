package com.redick.example.httpclient;


import com.redick.support.httpclient.TraceIdIdHttpClient5Interceptor;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author liupenghui
 * @date 2021/12/12 10:02 下午
 */
public class HttpClient5Util {

    private static final long CONNECT_TIMEOUT = 35000;

    private static final long CONNECTION_REQUEST_TIMEOUT = 35000;

    private static final long RESPONSE_TIMEOUT = 60000;

    public static String doGet(String url) {
        String result = "";
        // 为HttpClient添加拦截器TraceIdHttpClientInterceptor
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().addRequestInterceptorFirst(new TraceIdIdHttpClient5Interceptor()).build()) {
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Timeout.of(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS))
                    .setConnectionRequestTimeout(Timeout.of(CONNECTION_REQUEST_TIMEOUT, TimeUnit.MILLISECONDS))
                    .setResponseTimeout(Timeout.of(RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS))
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                // 通过返回对象获取返回数据
                HttpEntity entity = response.getEntity();
                // 通过EntityUtils中的toString方法将结果转换为字符串
                result = EntityUtils.toString(entity);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String doPost(String url, String param) {
        String result = "";
        System.out.println(param);
        // 为HttpClient添加拦截器TraceIdHttpClientInterceptor
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().addRequestInterceptorLast(new TraceIdIdHttpClient5Interceptor()).build()) {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Timeout.of(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS))
                    .setConnectionRequestTimeout(Timeout.of(CONNECTION_REQUEST_TIMEOUT, TimeUnit.MILLISECONDS))
                    .setResponseTimeout(Timeout.of(RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS))
                    .build();
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(param, StandardCharsets.UTF_8);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(stringEntity);
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // 通过EntityUtils中的toString方法将结果转换为字符串
                result = EntityUtils.toString(response.getEntity());
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

}
