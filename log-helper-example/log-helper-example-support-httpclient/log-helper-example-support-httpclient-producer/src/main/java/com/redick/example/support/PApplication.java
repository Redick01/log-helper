package com.redick.example.support;

import com.redick.starter.annotation.LogHelperEnable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Redick01
 *  2022/3/31 13:24
 */
@SpringBootApplication
@LogHelperEnable
public class PApplication {

    public static void main(String[] args) {
        SpringApplication.run(PApplication.class,args);
    }
}
