package com.redick.starter.configure;

import com.redick.starter.interceptor.InputChannelTraceInterceptor;
import com.redick.starter.interceptor.OutputChannelTraceInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.GlobalChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptor;

/**
 * @author Redick01
 */
@Configuration
@ConditionalOnClass(value = {ChannelInterceptor.class})
public class SpringCloudStreamTraceConfiguration {

    @Bean
    @GlobalChannelInterceptor(patterns = {"*-out-*"})
    public OutputChannelTraceInterceptor outputChannelTraceInterceptor() {
        return new OutputChannelTraceInterceptor();
    }

    @Bean
    @GlobalChannelInterceptor(patterns = {"*-in-*"})
    public InputChannelTraceInterceptor inputChannelTraceInterceptor() {
        return new InputChannelTraceInterceptor();
    }
}
