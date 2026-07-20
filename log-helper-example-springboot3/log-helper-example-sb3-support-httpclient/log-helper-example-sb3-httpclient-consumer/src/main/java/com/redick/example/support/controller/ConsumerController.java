/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redick.example.support.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.redick.annotation.LogMarker;
import com.redick.example.dto.RequestDTO;
import com.redick.example.dto.ResponseDTO;
import com.redick.example.httpclient.HttpClient5Util;
import com.redick.example.httpclient.HttpClientUtil;
import com.redick.example.okhttp.OkHttp3Util;
//import com.redick.example.okhttp.OkHttpUtil;
import com.redick.example.okhttp.OkHttpUtil;
import com.redick.example.support.service.ForestClientService;
import com.redick.support.hutoolhttpclient.TraceIdHutoolHttpClientInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private ForestClientService forestClientService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/httpclient")
    @LogMarker(businessDescription = "/httpclient-test",
            interfaceName = "com.redick.example.support.controller.ConsumerController#httpclient()")
    public @ResponseBody
    ResponseDTO httpclient(@RequestBody RequestDTO requestDTO) {
        return JSONObject.parseObject(HttpClientUtil.doPost(url, JSONObject.toJSONString(requestDTO)),
                ResponseDTO.class);
    }

    @PostMapping("/httpclient5")
    @LogMarker(businessDescription = "/httpclient5-test",
            interfaceName = "com.redick.example.support.controller.ConsumerController#httpclient5()")
    public @ResponseBody
    ResponseDTO httpclient5(@RequestBody RequestDTO requestDTO) {
        return JSONObject.parseObject(HttpClient5Util.doPost(url, JSONObject.toJSONString(requestDTO)),
                ResponseDTO.class);
    }

    @PostMapping("/okhttp")
    @LogMarker(businessDescription = "/okhttp-test",
            interfaceName = "com.redick.example.support.controller.ConsumerController#okhttp()")
    public @ResponseBody
    ResponseDTO okhttp(@RequestBody RequestDTO requestDTO) throws IOException {
        return JSONObject.parseObject(OkHttpUtil.doPost(url, JSONObject.toJSONString(requestDTO)), ResponseDTO.class);
    }

    @PostMapping("/okhttp3")
    @LogMarker(businessDescription = "/okhttp3-test",
            interfaceName = "com.redick.example.support.controller.ConsumerController#okhttp3()")
    public @ResponseBody
    ResponseDTO okhttp3(@RequestBody RequestDTO requestDTO) throws IOException {
        return JSONObject.parseObject(OkHttp3Util.doPost(url, JSONObject.toJSONString(requestDTO)), ResponseDTO.class);
    }

    @PostMapping("/hutool-httpclient")
    @LogMarker(businessDescription = "/hutool-httpclient-test",
            interfaceName = "com.redick.example.support.controller.ConsumerController#hutoolHttpclient()")
    public @ResponseBody
    ResponseDTO hutoolHttpclient(@RequestBody RequestDTO requestDTO) {
        TraceIdHutoolHttpClientInterceptor interceptor = new TraceIdHutoolHttpClientInterceptor();
        HttpRequest httpRequest = new HttpRequest(url)
                .method(Method.POST)
                .body(JSONObject.toJSONString(requestDTO))
                .timeout(2000);
        httpRequest.addInterceptor(interceptor);
        return JSONObject.parseObject(httpRequest.execute().body(), ResponseDTO.class);
    }

    @PostMapping("/forest-httpclient")
    @LogMarker(businessDescription = "/forest-httpclient-test",
            interfaceName = "com.redick.example.support.controller.ConsumerController#forestHttpclient()")
    public @ResponseBody
    ResponseDTO forestHttpclient(@RequestBody RequestDTO requestDTO) {
        return JSONObject.parseObject(forestClientService.say(requestDTO), ResponseDTO.class);
    }

    @PostMapping("/restTemplateTest")
    @LogMarker(businessDescription = "/restTemplate-test",
            interfaceName = "com.redick.example.support.controller.ConsumerController#restTemplateTest()")
    public @ResponseBody
    ResponseDTO restTemplateTest(@RequestBody RequestDTO requestDTO) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(requestDTO), httpHeaders);
        ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(url, httpEntity, ResponseDTO.class);
        return new ResponseDTO(0000, "success", responseEntity.getBody());
    }

}
