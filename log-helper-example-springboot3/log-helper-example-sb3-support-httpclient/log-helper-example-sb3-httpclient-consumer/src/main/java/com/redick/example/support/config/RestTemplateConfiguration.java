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

package com.redick.example.support.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.redick.support.resttemplate.TraceIdRestTemplateInterceptor;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author Redick01
 */
@Configuration
public class RestTemplateConfiguration {

    /**
     * 连接超时时间
     */
    private int connectionTimeout = 500;
    /**
     * 请求超时时间
     */
    private int requestTimeout = 500;
    /**
     * rest连接池连接个数
     */
    private int poolSize = 500;
    /**
     * rest每个路由最大连接数
     */
    private int defaultMaxPerRoute = 500;

    @Bean
    public HttpClient configHttpClient() {
        //连接池构造器
        PoolingHttpClientConnectionManager poolManager = new PoolingHttpClientConnectionManager();
        //连接池维护的最大连接数 默认：20
        poolManager.setMaxTotal(poolSize);
        //单路由最大连接数 默认：2
        poolManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        //禁用http重试
        HttpRequestRetryStrategy requestRetryStrategy = new DefaultHttpRequestRetryStrategy(0,
                TimeValue.ZERO_MILLISECONDS);
        //定期清理无效连接
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, (ThreadFactory) Thread::new);
        //配置超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(connectionTimeout))
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(requestTimeout))
                .build();

        HttpClient httpClient = HttpClients.custom()
                .setConnectionManager(poolManager)
                .setConnectionManagerShared(true)
                .setRetryStrategy(requestRetryStrategy)
                .setDefaultRequestConfig(requestConfig)
                .build();

        executorService.scheduleAtFixedRate(() -> {
            poolManager.closeExpired();
            poolManager.closeIdle(TimeValue.ofSeconds(30));
        }, 0, 5, TimeUnit.SECONDS);

        return httpClient;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory configHttpRequestFactory(HttpClient httpClient) {
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Bean
    public RestTemplate configRestTemplate(ClientHttpRequestFactory requestFactory) {

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        List<HttpMessageConverter<?>> results = restTemplate.getMessageConverters();
        for (HttpMessageConverter messageConverter : results) {
            if (messageConverter instanceof MappingJackson2HttpMessageConverter) {
                //restTemplate能兼容报文字段不匹配时仍能正确处理请求的关键性配置
                ((MappingJackson2HttpMessageConverter) messageConverter).getObjectMapper().configure(
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            }
        }
        restTemplate.setInterceptors(Stream.of(new TraceIdRestTemplateInterceptor()).collect(
                Collectors.toList()));
        return restTemplate;
    }
}
