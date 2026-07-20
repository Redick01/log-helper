/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redick.loghelper.config;

import com.redick.executor.TtlThreadPoolExecutor;
import com.redick.executor.TtlThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Redick01
 *  2022/3/29 17:36
 */
@Configuration
public class ThreadPoolConfiguration {

    private static final int CORE = 5;

    private static final int MAX = 10;

    private static final int KEEP_ALIVE = 30;

    private static final int SIZE = 1000;

    /**
     * 线程池bean
     *
     * @return 线程池实例
     */
    @Bean(name = "ttlThreadPoolExecutor")
    public TtlThreadPoolExecutor ttlThreadPoolExecutor() {
        return new TtlThreadPoolExecutor(CORE, MAX, KEEP_ALIVE, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(SIZE));
    }

    /**
     * 线程池bean
     *
     * @return 线程池实例
     */
    @Bean(name = "ttlThreadPoolTaskExecutor")
    public TtlThreadPoolTaskExecutor ttlThreadPoolTaskExecutor() {
        return new TtlThreadPoolTaskExecutor();
    }

    /**
     * 线程池bean
     *
     * @return 线程池实例
     */
    @Bean(name = "threadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(CORE, MAX, KEEP_ALIVE, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(SIZE));
    }
}
