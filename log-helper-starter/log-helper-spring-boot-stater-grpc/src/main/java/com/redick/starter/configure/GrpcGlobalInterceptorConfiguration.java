package com.redick.starter.configure;

import com.redick.starter.interceptor.GrpcInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Redick01
 */
@Configuration
public class GrpcGlobalInterceptorConfiguration {

    @Bean
    public GrpcInterceptor grpcInterceptor() {
        return new GrpcInterceptor();
    }
}
