package com.redick.example;

import com.redick.starter.annotation.LogHelperEnable;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Redick01
 *  2022/3/29 00:11
 */
@SpringBootApplication
@LogHelperEnable
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableDubbo(scanBasePackages = "com.redick.example.consumer")
@PropertySource("classpath:/spring/dubbo-consumer.properties")
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
