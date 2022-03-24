package com.redick.rocketmq;

import com.redick.MqWrapperBean;
import com.redick.common.GlobalSessionIdDefine;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author liupenghui
 * @date 2021/12/10 1:24 下午
 */
@SuppressWarnings("all")
public class MqConsumerProcessor {


    /**
     * 非阿里云RocketMQ消费
     * @param mqWrapperBean {@link MqWrapperBean}
     * @param mqConsumer {@link MqConsumer}
     */
    @Trace
    public static void process(MqWrapperBean mqWrapperBean, MqConsumer mqConsumer) {
        try {
            String traceId = mqWrapperBean.getTraceId();
            if (StringUtils.isBlank(traceId)) {
                traceId = TraceContext.traceId();
                if (StringUtils.isBlank(traceId)) {
                    traceId = UUID.randomUUID().toString();
                }
            }
            MDC.put(GlobalSessionIdDefine.GLOBAL_SESSION_ID_KEY, traceId);
            mqConsumer.consume(mqWrapperBean.getT());
        } finally {
            MDC.clear();
        }
    }

    /**
     *
     * @param mqWrapperBean {@link MqWrapperBean}
     * @param mqConsumer {@link MqConsumer}
     * @return {@link RocketMQLocalTransactionState}
     */
    @Trace
    public static RocketMQLocalTransactionState processLocalTransaction(MqWrapperBean mqWrapperBean, MqConsumer mqConsumer) {
        try {
            String traceId = mqWrapperBean.getTraceId();
            if (StringUtils.isBlank(traceId)) {
                traceId = TraceContext.traceId();
                if (StringUtils.isBlank(traceId)) {
                    traceId = UUID.randomUUID().toString();
                }
            }
            MDC.put(GlobalSessionIdDefine.GLOBAL_SESSION_ID_KEY, traceId);
            return mqConsumer.localTransactionConsume(mqWrapperBean.getT());
        } finally {
            MDC.clear();
        }
    }
}
