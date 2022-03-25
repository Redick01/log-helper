package com.redick.starter.configure;

import com.redick.AroundLogHandler;
import com.redick.starter.interceptor.FeignRequestInterceptor;
import com.redick.starter.interceptor.SpringWebMvcInterceptor;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author Redick01
 * @date 2022/3/25 14:42
 */
@Configuration
public class LogHandlerAutoConfiguration {

    @Bean
    public AroundLogHandler aroundLogHandler() {
        return new AroundLogHandler();
    }

    @Bean
    @ConditionalOnClass(RequestInterceptor.class)
    public FeignRequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }

    @Bean
    @ConditionalOnClass(DispatcherServlet.class)
    public SpringWebMvcInterceptor springWebMvcInterceptor() {
        return new SpringWebMvcInterceptor();
    }
}
