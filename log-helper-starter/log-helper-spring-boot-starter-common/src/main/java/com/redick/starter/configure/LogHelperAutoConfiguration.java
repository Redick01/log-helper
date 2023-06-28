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

package com.redick.starter.configure;

import com.redick.AroundLogHandler;
import com.redick.banner.LogHelperBanner;
import com.redick.starter.interceptor.SpringWebMvcInterceptor;
import com.redick.starter.processor.SpringRedisConnectionFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Redick01
 *  2022/3/25 14:42
 */
@Configuration
public class LogHelperAutoConfiguration {

    @Bean
    public LogHelperBanner logHelperBanner() {
        return new LogHelperBanner();
    }

    @Bean
    public AroundLogHandler aroundLogHandler() {
        return new AroundLogHandler();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.web.servlet.DispatcherServlet")
    public WebMvcConfigurer webMvcInterceptor() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new SpringWebMvcInterceptor());
            }
        };
    }

    @Bean
    @ConditionalOnProperty()
    public SpringRedisConnectionFactoryPostProcessor springRedisConnectionFactoryPostProcessor() {
        return new SpringRedisConnectionFactoryPostProcessor();
    }
}
