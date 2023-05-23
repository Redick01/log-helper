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
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
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
     * socket超时时间
     */
    private int socketTimeout = 500;
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
        HttpRequestRetryHandler requestRetryHandler = new DefaultHttpRequestRetryHandler(0, false);
        //定期清理无效连接
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, (ThreadFactory) Thread::new);
        //配置超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectionTimeout)
                .setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(requestTimeout)
                .build();

        HttpClient httpClient = HttpClients.custom()
                .setConnectionManager(poolManager)
                .setConnectionManagerShared(true)
                .setRetryHandler(requestRetryHandler)
                .setDefaultRequestConfig(requestConfig)
                .build();

        executorService.scheduleAtFixedRate(() -> {
            poolManager.closeExpiredConnections();
            poolManager.closeIdleConnections(30, TimeUnit.SECONDS);
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
