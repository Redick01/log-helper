package com.ruubypay.log.filter.mq;

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
}
