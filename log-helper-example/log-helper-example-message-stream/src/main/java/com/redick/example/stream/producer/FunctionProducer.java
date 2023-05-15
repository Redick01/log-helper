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
