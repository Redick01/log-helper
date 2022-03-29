package com.redick.example.okhttp;

import com.redick.support.okhttp.TraceIdOkhttpInterceptor;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * @author liupenghui
 * @date 2021/12/12 10:06 下午
 */
public class OkHttpExample {

    public static void main(String[] args) throws IOException {
        String url = "http://127.0.0.1:8081/order/getPayCount?orderNo=1";
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new TraceIdOkhttpInterceptor());
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
    }
}
