package com.ruubypay.log.filter.web;

import org.springframework.web.servlet.config.annotation.*;

/**
 * @author liupenghui
 * @date 2021/6/30 11:41 上午
 */
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 日志链路追踪拦截器
        registry.addInterceptor(new WebHandlerMethodInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
