package com.redick.example.support;

import com.dtflys.forest.springboot.annotation.ForestScan;
import com.redick.starter.annotation.LogHelperEnable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Redick01
 *  2022/3/31 14:29
 */
@SpringBootApplication
@LogHelperEnable
@ForestScan(basePackages = "com.redick.example.support.service")
public class CApplication {


    public static void main(String[] args) {
        SpringApplication.run(CApplication.class, args);
    }
}
