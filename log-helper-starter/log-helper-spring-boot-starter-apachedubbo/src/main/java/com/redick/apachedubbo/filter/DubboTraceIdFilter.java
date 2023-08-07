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

package com.redick.apachedubbo.filter;

import static com.redick.constant.TraceTagConstant.DUBBO_INVOKE_AFTER;
import static com.redick.constant.TraceTagConstant.DUBBO_INVOKE_BEFORE;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.RpcContext;

/**
 * @author liupenghui
 *  2020/12/26 11:47 下午
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER_SIDE, CommonConstants.CONSUMER_SIDE})
public class DubboTraceIdFilter extends AbstractInterceptor implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String side = invoker.getUrl().getParameter(CommonConstants.SIDE_KEY);
        try {
            if (CommonConstants.CONSUMER_SIDE.equals(side)) {
                log.info(LogUtil.marker(invocation.getArguments()), "调用接口[{}]的方法[{}]",
                        invoker.getInterface().getSimpleName(),
                        invocation.getMethodName());
                executeBefore(DUBBO_INVOKE_BEFORE);
                // consumer set trace information to attachment
                String traceId = traceId();
                if (StringUtils.isNotBlank(traceId)) {
                    RpcContext.getServiceContext().setAttachment(Tracer.TRACE_ID, traceId);
                    RpcContext.getServiceContext().setAttachment(Tracer.SPAN_ID, spanId());
                    RpcContext.getServiceContext().setAttachment(Tracer.PARENT_ID, parentId());
                }
            } else {
                // provider get trace information form attachment
                String traceId = RpcContext.getServiceContext().getAttachment(Tracer.TRACE_ID);
                String spanId = RpcContext.getServiceContext().getAttachment(Tracer.SPAN_ID);
                String parentId = RpcContext.getServiceContext().getAttachment(Tracer.PARENT_ID);
                Tracer.trace(traceId, spanId, parentId);
            }
            return invoker.invoke(invocation);
        } finally {
            executeAfter(DUBBO_INVOKE_AFTER);
        }
    }
}
