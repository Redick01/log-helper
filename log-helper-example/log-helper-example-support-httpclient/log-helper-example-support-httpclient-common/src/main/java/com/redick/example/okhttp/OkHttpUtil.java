package com.redick.example.okhttp;

import com.redick.support.okhttp.TraceIdOkhttpInterceptor;
import com.squareup.okhttp.*;

import java.io.IOException;

/**
 * @author liupenghui
 *  2021/12/12 10:06 下午
 */
public class OkHttpUtil {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient client = null;

    static {
        client = new OkHttpClient();
        client.interceptors().add(new TraceIdOkhttpInterceptor());
    }


    public static String doGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().toString();
    }

    public static String doPost(String url, String param) throws IOException {
        RequestBody body = RequestBody.create(JSON, param);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
