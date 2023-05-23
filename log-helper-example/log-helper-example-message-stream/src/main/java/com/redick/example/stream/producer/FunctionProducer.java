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

package com.redick.example.stream.producer;

import com.redick.example.stream.entity.Stock;
import com.redick.util.LogUtil;
import java.time.Duration;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

/**
 * @author Redick01
 */
@Configuration
@Slf4j
public class FunctionProducer {

    @Bean
    public Supplier<Flux<Stock>> producer() {
        return () -> Flux.interval(Duration.ofSeconds(2)).map(s -> {
            Stock stock = new Stock();
            stock.setProductId("123");
            stock.setProductName("HUAWEI手机");
            stock.setTotalCount(1000);
            return stock;
        }).log();
    }
}
