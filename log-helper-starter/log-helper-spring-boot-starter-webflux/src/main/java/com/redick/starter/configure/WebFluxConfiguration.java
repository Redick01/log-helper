package com.redick.starter.configure;

import com.redick.starter.filter.LogHelperWebFluxFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * @author Redick01
 */
@Configuration
public class WebFluxConfiguration {

    @Bean
    @ConditionalOnClass(WebFluxConfigurer.class)
    public LogHelperWebFluxFilter logHelperWebFluxFilter() {
        return new LogHelperWebFluxFilter();
    }
}
