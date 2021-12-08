package com.ruubypay.log.filter.web;

import org.springframework.web.servlet.config.annotation.*;

/**
 * @author liupenghui
 * @date 2021/6/30 11:41 上午
 */
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration;
        interceptorRegistration = registry.addInterceptor(new WebHandlerMethodInterceptor());
        interceptorRegistration.order(1);
    }
}
