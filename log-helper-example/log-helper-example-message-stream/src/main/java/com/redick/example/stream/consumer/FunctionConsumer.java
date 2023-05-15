package com.redick.example.stream.consumer;

import com.redick.example.stream.entity.Stock;
import com.redick.util.LogUtil;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Redick01
 */
@Configuration
@Slf4j
public class FunctionConsumer {

    @Bean
    public Consumer<Stock> consumer() {
        return stock -> {
            log.info("222222222222");
            log.info(LogUtil.marker(stock), "收到消息");
        };
    }
}
