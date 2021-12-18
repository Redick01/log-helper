package com.redick.boot.config;

import com.redick.web.WebConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liupenghui
 * @date 2021/12/8 3:23 下午
 */
@Configuration
@ConditionalOnClass({DispatcherServlet.class, WebMvcConfigurer.class})
public class WebInterceptorConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "webConfiguration")
    public WebConfiguration webConfiguration() {
        return new WebConfiguration();
    }
}
