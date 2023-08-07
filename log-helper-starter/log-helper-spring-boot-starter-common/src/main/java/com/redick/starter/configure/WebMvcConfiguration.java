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

import com.redick.starter.interceptor.SpringWebMvcInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;

import java.util.List;

/**
 * @author: Redick01
 * @date: 2023/8/4 15:08
 */
@ConditionalOnClass(value = {DispatcherServlet.class})
@Configuration
public class WebMvcConfiguration {

    /**
     * webmvc configuration
     *
     * @return WebMvcConfigurer
     */
    @Bean
    @ConditionalOnClass(name = "org.springframework.web.servlet.DispatcherServlet")
    public WebMvcConfigurer webMvcInterceptor() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new SpringWebMvcInterceptor());
            }

            @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {
            }

            @Override
            public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
            }

            @Override
            public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
            }

            @Override
            public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
            }

            @Override
            public void addFormatters(FormatterRegistry registry) {
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
            }

            @Override
            public void addCorsMappings(CorsRegistry registry) {
            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
            }

            @Override
            public void configureViewResolvers(ViewResolverRegistry registry) {
            }

            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            }

            @Override
            public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
            }

            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            }

            @Override
            public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
            }

            @Override
            public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
            }

            @Override
            public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
            }

            @Override
            public Validator getValidator() {
                return null;
            }

            @Override
            public MessageCodesResolver getMessageCodesResolver() {
                return null;
            }
        };
    }
}
