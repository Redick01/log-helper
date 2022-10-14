package io.redick.example;

import com.redick.starter.annotation.LogHelperEnable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Redick01
 */
@SpringBootApplication
@MapperScan(basePackages = {"io.redick.example.dao"})
@LogHelperEnable
public class ApplicationMysql {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMysql.class, args);
    }
}
