package com.redick.example.motan;

import com.redick.starter.annotation.LogHelperEnable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Redick01
 */
@SpringBootApplication
@LogHelperEnable
@EnableAspectJAutoProxy
@ImportResource(value = {"classpath:motan-server.xml"})
public class ApplicationSvr {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationSvr.class, args);
    }
}
