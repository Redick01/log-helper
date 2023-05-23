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

package com.redick.support.mq;

import com.redick.constant.TraceIdDefine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * MQ消息Bean的包装Bean
 * @author liupenghui
 *  2021/12/10 1:22 下午
 */
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class MqWrapperBean<T> {

    private String traceId;

    /**
     * MQ业务数据
     */
    private T t;

    public MqWrapperBean(T t) {
        this.t = t;
        String newTraceId = MDC.get(TraceIdDefine.TRACE_ID);
        if (StringUtils.isNotBlank(newTraceId)) {
            this.traceId = newTraceId;
        }
    }
}
