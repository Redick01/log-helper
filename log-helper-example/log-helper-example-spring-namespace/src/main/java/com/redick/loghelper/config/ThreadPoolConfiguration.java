package com.redick.loghelper.config;

import com.redick.executor.TtlThreadPoolExecutor;
import com.redick.executor.TtlThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Redick01
 * @date 2022/3/29 17:36
 */
@Configuration
public class ThreadPoolConfiguration {

    @Bean(name = "ttlThreadPoolExecutor")
    public TtlThreadPoolExecutor ttlThreadPoolExecutor() {
        return new TtlThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000));
    }

    @Bean(name = "ttlThreadPoolTaskExecutor")
    public TtlThreadPoolTaskExecutor ttlThreadPoolTaskExecutor() {
        return new TtlThreadPoolTaskExecutor();
    }
}
