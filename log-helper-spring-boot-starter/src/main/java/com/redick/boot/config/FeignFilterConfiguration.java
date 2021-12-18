package com.redick.boot.config;

import com.redick.openfeign.interceptor.FeignRequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liupenghui
 * @date 2021/12/8 2:43 下午
 */
@Configuration
@ConditionalOnClass(RequestTemplate.class)
public class FeignFilterConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "feignRequestFilter")
    public FeignRequestInterceptor feignRequestFilter() {
        return new FeignRequestInterceptor();
    }
}
