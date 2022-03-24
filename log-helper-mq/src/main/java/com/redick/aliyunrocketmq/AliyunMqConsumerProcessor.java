package com.redick.aliyunrocketmq;

import com.aliyun.openservices.ons.api.Action;
import com.redick.MqWrapperBean;
import com.redick.common.GlobalSessionIdDefine;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author liupenghui
 * @date 2021/12/10 1:24 下午
 */
public class AliyunMqConsumerProcessor {

    /**
     * 阿里云RocketMQ消费
     * @param mqWrapperBean {@link MqWrapperBean}
     * @param mqConsumer {@link AliyunMqConsumer}
     * @return {@link Action}
     */
    @Trace
    @SuppressWarnings("all")
    public static Action process(MqWrapperBean mqWrapperBean, AliyunMqConsumer mqConsumer) {
        try {
            String traceId = mqWrapperBean.getTraceId();
            if (StringUtils.isBlank(traceId)) {
                traceId = TraceContext.traceId();
                if (StringUtils.isBlank(traceId)) {
                    traceId = UUID.randomUUID().toString();
                }
            }
            MDC.put(GlobalSessionIdDefine.GLOBAL_SESSION_ID_KEY, traceId);
            return mqConsumer.consume(mqWrapperBean.getT());
        } finally {
            MDC.clear();
        }
    }
}
