package com.redick.example.stream;

import com.redick.starter.annotation.LogHelperEnable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Redick01
 */
@SpringBootApplication
@LogHelperEnable
public class StreamApplication {

    public static void main( String[] args ) {
        SpringApplication.run(StreamApplication.class, args);
    }
}
