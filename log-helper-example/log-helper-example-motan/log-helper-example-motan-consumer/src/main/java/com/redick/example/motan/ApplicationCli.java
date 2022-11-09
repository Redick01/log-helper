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
@ImportResource(value = {"classpath:motan-client.xml"})
public class ApplicationCli {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationCli.class, args);
    }
}
