package com.redick.example.support.controller;

import com.alibaba.fastjson.JSONObject;
import com.redick.annotation.LogMarker;
import com.redick.example.dto.Request;
import com.redick.example.dto.Response;
import com.redick.example.httpclient.HttpClient5Util;
import com.redick.example.httpclient.HttpClientUtil;
import com.redick.example.okhttp.OkHttp3Util;
import com.redick.example.okhttp.OkHttpUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Redick01
 *  2022/3/31 17:35
 */
@RestController
public class ConsumerController {

    /**
     *
     *
     * 注：okhttp和okhttp3不能同时使用和运行示例，否则会导致okhttp3无法使用并且出现java.lang.NoClassDefFoundError:异常
     *
     *
     */

    private final String url = "http://localhost:8783/producer/say";

    @PostMapping("/httpclient")
    @LogMarker(businessDescription = "/httpclient-test", interfaceName = "com.redick.example.support.controller.ConsumerController#httpclient()")
    public @ResponseBody
    Response httpclient(@RequestBody Request request) {
        return JSONObject.parseObject(HttpClientUtil.doPost(url, JSONObject.toJSONString(request)), Response.class);
    }

    @PostMapping("/httpclient5")
    @LogMarker(businessDescription = "/httpclient5-test", interfaceName = "com.redick.example.support.controller.ConsumerController#httpclient5()")
    public @ResponseBody
    Response httpclient5(@RequestBody Request request) {
        return JSONObject.parseObject(HttpClient5Util.doPost(url, JSONObject.toJSONString(request)), Response.class);
    }

    @PostMapping("/okhttp")
    @LogMarker(businessDescription = "/okhttp-test", interfaceName = "com.redick.example.support.controller.ConsumerController#okhttp()")
    public @ResponseBody
    Response okhttp(@RequestBody Request request) throws IOException {
        return JSONObject.parseObject(OkHttpUtil.doPost(url, JSONObject.toJSONString(request)), Response.class);
    }

    @PostMapping("/okhttp3")
    @LogMarker(businessDescription = "/okhttp3-test", interfaceName = "com.redick.example.support.controller.ConsumerController#okhttp3()")
    public @ResponseBody
    Response okhttp3(@RequestBody Request request) throws IOException {
        return JSONObject.parseObject(OkHttp3Util.doPost(url, JSONObject.toJSONString(request)), Response.class);
    }
}
