package com.redick.example.okhttp;

import com.redick.support.okhttp.TraceIdOkhttp3Interceptor;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author liupenghui
 * @date 2021/12/12 10:10 下午
 */
public class OkHttp3Example {

    public static void main(String[] args) throws IOException {
        String url = "http://127.0.0.1:8081/order/getPayCount?orderNo=1";
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new TraceIdOkhttp3Interceptor())
                .build();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
    }
}
