package com.redick.starter.configure;

import com.redick.starter.client.FeignTraceClientWrapper;
import feign.Client;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.cloud.openfeign.loadbalancer.OnRetryNotEnabledCondition;
import org.springframework.cloud.openfeign.loadbalancer.RetryableFeignBlockingLoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author Redick01
 * 2022/3/26 14:32
 */
@Configuration
public class OpenFeignConfiguration {

    @Bean
    @Conditional(OnRetryNotEnabledCondition.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public Client feignClient(LoadBalancerClient loadBalancerClient,
            LoadBalancerClientFactory loadBalancerClientFactory) {
        return new FeignBlockingLoadBalancerClient(new FeignTraceClientWrapper(new Client.Default(null, null))
                , loadBalancerClient, loadBalancerClientFactory);
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.retry.support.RetryTemplate")
    @ConditionalOnBean(LoadBalancedRetryFactory.class)
    @ConditionalOnProperty(value = "spring.cloud.loadbalancer.retry.enabled", havingValue = "true",
            matchIfMissing = true)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public Client feignRetryClient(LoadBalancerClient loadBalancerClient,
            LoadBalancedRetryFactory loadBalancedRetryFactory, LoadBalancerClientFactory loadBalancerClientFactory) {
        return new RetryableFeignBlockingLoadBalancerClient(new FeignTraceClientWrapper(new Client.Default(null, null))
                , loadBalancerClient, loadBalancedRetryFactory, loadBalancerClientFactory);
    }
}
