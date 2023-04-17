package com.redick.starter.configure;

import com.redick.banner.LogHelperBanner;
import com.redick.starter.filter.TracerFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Redick01
 */
@Configuration
public class ScgTraceAutoConfiguration {

    @Bean
    public LogHelperBanner logHelperBanner() {
        return new LogHelperBanner();
    }

    @Bean
    public TracerFilter tracerFilter() {
        return new TracerFilter();
    }
}
