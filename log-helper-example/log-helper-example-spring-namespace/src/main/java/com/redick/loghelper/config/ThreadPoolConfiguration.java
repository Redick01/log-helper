package com.redick.loghelper.config;

import com.redick.executor.ThreadPoolBuilder;
import com.redick.executor.TtlThreadPoolExecutor;
import com.redick.executor.TtlThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Redick01
 * @date 2022/3/29 17:36
 */
@Configuration
public class ThreadPoolConfiguration {

//    @Bean(name = "executorService")
//    public ExecutorService executorService() {
//        return ThreadPoolBuilder.builder()
//                .corePoolSize(5)
//                .maximumPoolSize(10)
//                .threadPoolName("ttl-threadBuilder-example")
//                .queueCapacity(1000)
//                .threadFactory("ttl-thread-builder")
//                .keepAliveTime(30)
//                .timeUnit(TimeUnit.SECONDS)
//                .rejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy())
//                .build();
//    }

    @Bean(name = "ttlThreadPoolExecutor")
    public TtlThreadPoolExecutor ttlThreadPoolExecutor() {
        return new TtlThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000));
    }

    @Bean(name = "ttlThreadPoolTaskExecutor")
    public TtlThreadPoolTaskExecutor ttlThreadPoolTaskExecutor() {
        return new TtlThreadPoolTaskExecutor();
    }
}
