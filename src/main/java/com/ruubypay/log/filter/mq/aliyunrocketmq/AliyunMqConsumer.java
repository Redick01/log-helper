package com.ruubypay.log.filter.mq.aliyunrocketmq;

import com.aliyun.openservices.ons.api.Action;

/**
 * @author liupenghui
 * @date 2021/12/10 4:59 下午
 */
public interface AliyunMqConsumer<T> {

    /**
     * 阿里云RocketMQ消费接口
     * @param t 业务数据
     * @return {@link Action}
     */
    Action consume(T t);
}
