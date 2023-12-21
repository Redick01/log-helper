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

package com.redick.alibabadubbo.filter;

import static com.redick.constant.TraceTagConstant.DUBBO_INVOKE_AFTER;
import static com.redick.constant.TraceTagConstant.DUBBO_INVOKE_BEFORE;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcContext;

/**
 * @author liupenghui
 *  2020/12/26 11:47 下午
 */
@Activate(group = {Constants.CONSUMER_SIDE, Constants.PROVIDER_SIDE})
@Slf4j
public class DubboxTraceIdFilter extends AbstractInterceptor implements Filter {

    @Trace
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            String side = invoker.getUrl().getParameter(Constants.SIDE_KEY);
            if (Constants.PROVIDER_SIDE.equals(side)) {
                // provider get trace information form attachment
                String traceId = RpcContext.getContext().getAttachment(LogUtil.kLOG_KEY_TRACE_ID);
                String spanId = RpcContext.getContext().getAttachment(Tracer.SPAN_ID);
                String parentId = RpcContext.getContext().getAttachment(Tracer.PARENT_ID);
                Tracer.trace(traceId, spanId, parentId);
            } else {
                executeBefore(DUBBO_INVOKE_BEFORE);
                // consumer set trace information to attachment
                String traceId = traceId();
                if (StringUtils.isNotBlank(traceId)) {
                    RpcContext.getContext().setAttachment(Tracer.TRACE_ID, traceId);
                    RpcContext.getContext().setAttachment(Tracer.SPAN_ID, spanId());
                    RpcContext.getContext().setAttachment(Tracer.PARENT_ID, parentId());
                }
            }
            return invoker.invoke(invocation);
        } finally {
            executeAfter(DUBBO_INVOKE_AFTER);
        }
    }
}
