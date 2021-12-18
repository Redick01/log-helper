package com.redick.web;

import com.redick.web.interceptor.WebHandlerMethodInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liupenghui
 * @date 2021/12/18 10:57 下午
 */
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration;
        interceptorRegistration = registry.addInterceptor(new WebHandlerMethodInterceptor());
        interceptorRegistration.order(1);
    }
}
