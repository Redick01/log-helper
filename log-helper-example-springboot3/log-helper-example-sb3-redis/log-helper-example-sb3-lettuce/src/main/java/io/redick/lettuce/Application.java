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

package io.redick.lettuce;

import com.redick.starter.annotation.LogHelperEnable;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author: Redick01
 * @date: 2023/6/27 15:09
 */
@SpringBootApplication
@LogHelperEnable
public class Application {

    @Bean
    public RedisCommands<String, String> redisCommands() {
        RedisClient redisClient = RedisClient.create("redis://@127.0.0.1:6379/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        return connection.sync();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
