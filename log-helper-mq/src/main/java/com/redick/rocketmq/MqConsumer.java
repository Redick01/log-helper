package com.redick.rocketmq;

import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;

/**
 * @author liupenghui
 * @date 2021/12/10 1:23 下午
 */
public interface MqConsumer<T> {

    /**
     * 非阿里云RocketMQ消费接口
     * @param t 业务数据
     */
    void consume(T t);

    /**
     * rocketmq 本地事务消费
     * @param t 业务数据
     * @return {@link RocketMQLocalTransactionState}
     */
    RocketMQLocalTransactionState localTransactionConsume(T t);
}
