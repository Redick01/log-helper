package com.redick.starter.filter;

import static com.redick.constant.TraceTagConstant.SCG_INVOKE_END;
import static com.redick.constant.TraceTagConstant.START_TIME;

import com.redick.support.AbstractInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Redick01
 */
@Slf4j
public class RtFilter extends AbstractInterceptor implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return chain.filter(exchange).transformDeferred(request -> {
            final long start = System.currentTimeMillis();
            return request.doFinally(s -> {
               MDC.put(START_TIME, String.valueOf(start));
               executeAfter(SCG_INVOKE_END);
            });
        });
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }
}
