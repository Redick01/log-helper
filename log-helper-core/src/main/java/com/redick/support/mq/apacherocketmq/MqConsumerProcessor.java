/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redick.support.mq.apacherocketmq;

import com.redick.constant.TraceIdDefine;
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
    public static RocketMQLocalTransactionState processLocalTransaction(MqWrapperBean mqWrapperBean,
                                                                        MqConsumer mqConsumer) {
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
