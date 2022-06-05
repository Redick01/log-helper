package com.redick.support.mq.apacherocketmq;

import com.redick.common.TraceIdDefine;
import com.redick.support.mq.MqWrapperBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author liupenghui
 *  2021/12/10 1:24 下午
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
                if (StringUtils.isBlank(traceId) || TraceIdDefine.SKYWALKING_NO_ID.equals(TraceContext.traceId())) {
                    traceId = UUID.randomUUID().toString();
                }
            }
            MDC.put(TraceIdDefine.TRACE_ID, traceId);
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
            MDC.put(TraceIdDefine.TRACE_ID, traceId);
            return mqConsumer.localTransactionConsume(mqWrapperBean.getT());
        } finally {
            MDC.clear();
        }
    }
}
