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

package com.redick.support.mq.aliyunrocketmq;

import com.aliyun.openservices.ons.api.Action;
import com.redick.constant.TraceIdDefine;
import com.redick.support.mq.MqWrapperBean;
import com.redick.util.TraceIdUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * @author liupenghui
 *  2021/12/10 1:24 下午
 */
public class AliyunMqConsumerProcessor {

    /**
     * 阿里云RocketMQ消费
     * @param mqWrapperBean {@link MqWrapperBean}
     * @param mqConsumer {@link AliyunMqConsumer}
     * @return {@link Action}
     */
    @SuppressWarnings("all")
    public static Action process(MqWrapperBean mqWrapperBean, AliyunMqConsumer mqConsumer) {
        try {
            String traceId = mqWrapperBean.getTraceId();
            if (StringUtils.isBlank(traceId)) {
                traceId = TraceIdUtil.traceId();
            }
            MDC.put(TraceIdDefine.TRACE_ID, traceId);
            return mqConsumer.consume(mqWrapperBean.getT());
        } finally {
            MDC.clear();
        }
    }
}
