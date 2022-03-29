package com.redick.example;

import com.redick.starter.annotation.LogHelperEnable;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

/**
 * @author Redick01
 * @date 2022/3/28 13:42
 */
@SpringBootApplication
@LogHelperEnable
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@RestController
@EnableDubbo(scanBasePackages = "com.redick.example.producer")
@PropertySource("classpath:/spring/dubbo-provider.properties")
public class ProducerApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ProducerApplication.class, args);
        new CountDownLatch(1).await();
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
