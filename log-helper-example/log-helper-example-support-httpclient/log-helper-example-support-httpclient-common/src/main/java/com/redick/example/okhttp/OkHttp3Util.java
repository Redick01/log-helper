package com.redick.example.okhttp;

import com.redick.support.okhttp3.TraceIdOkhttp3JavaInterceptor;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author liupenghui
 *  2021/12/12 10:10 下午
 */
public class OkHttp3Util {

    private static final long CONNECT_TIMEOUT = 35;

    private static final long READ_TIMEOUT = 35;

    private static final long WRITE_TIMEOUT = 60;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient client = null;

    static {
        client = new OkHttpClient().newBuilder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new TraceIdOkhttp3JavaInterceptor())
                .build();
    }


    public static String doGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).toString();
        }
    }

    public static String doPost(String url, String param) throws IOException {
        RequestBody body = RequestBody.create(param, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }
}
