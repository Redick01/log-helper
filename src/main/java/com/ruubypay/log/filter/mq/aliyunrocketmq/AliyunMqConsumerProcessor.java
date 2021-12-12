package com.ruubypay.log.filter.mq.apacherocketmq;

import com.aliyun.openservices.ons.api.Action;
import com.ruubypay.log.common.GlobalSessionIdDefine;
import com.ruubypay.log.filter.mq.MqWrapperBean;
import com.ruubypay.log.filter.mq.aliyunrocketmq.AliyunMqConsumer;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author liupenghui
 * @date 2021/12/10 1:24 下午
 */
public class MqConsumerProcessor {

    /**
     * 阿里云RocketMQ消费
     * @param mqWrapperBean {@link MqWrapperBean}
     * @param mqConsumer {@link MqConsumer}
     * @return {@link Action}
     */
    @Trace
    public static Action aliProcess(MqWrapperBean mqWrapperBean, AliyunMqConsumer mqConsumer) {
        try {
            String traceId = mqWrapperBean.getTraceId();
            if (StringUtils.isBlank(traceId)) {
                traceId = TraceContext.traceId();
                if (StringUtils.isBlank(traceId)) {
                    traceId = UUID.randomUUID().toString();
                }
            }
            MDC.put(GlobalSessionIdDefine.kGLOBAL_SESSION_ID_KEY, traceId);
            return mqConsumer.aliConsume(mqWrapperBean.getT());
        } finally {
            MDC.clear();
        }
    }

    /**
     * 非阿里云RocketMQ消费
     * @param mqWrapperBean {@link MqWrapperBean}
     * @param mqConsumer {@link MqConsumer}
     */
    public static void process(MqWrapperBean mqWrapperBean, MqConsumer mqConsumer) {
        try {
            String traceId = mqWrapperBean.getTraceId();
            if (StringUtils.isBlank(traceId)) {
                traceId = TraceContext.traceId();
                if (StringUtils.isBlank(traceId)) {
                    traceId = UUID.randomUUID().toString();
                }
            }
            MDC.put(GlobalSessionIdDefine.kGLOBAL_SESSION_ID_KEY, traceId);
            mqConsumer.consume(mqWrapperBean.getT());
        } finally {
            MDC.clear();
        }
    }
}
