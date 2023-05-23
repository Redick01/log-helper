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

package com.redick.motan.filter;

import static com.redick.constant.TraceTagConstant.MOTAN_CALL_AFTER;
import static com.redick.constant.TraceTagConstant.MOTAN_CALL_BEFORE;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.common.URLParamType;
import com.weibo.api.motan.core.extension.Activation;
import com.weibo.api.motan.core.extension.Scope;
import com.weibo.api.motan.core.extension.Spi;
import com.weibo.api.motan.core.extension.SpiMeta;
import com.weibo.api.motan.filter.Filter;
import com.weibo.api.motan.rpc.Caller;
import com.weibo.api.motan.rpc.Request;
import com.weibo.api.motan.rpc.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Redick01
 */
@Spi(scope = Scope.SINGLETON)
@SpiMeta(name = "log_helper_filter")
@Activation(key = { MotanConstants.NODE_TYPE_SERVICE, MotanConstants.NODE_TYPE_REFERER })
@Slf4j
public class MotanTracerFilter extends AbstractInterceptor implements Filter {

    @Override
    public Response filter(Caller<?> caller, Request request) {
        String nodeName = caller.getUrl().getParameter(URLParamType.nodeType.getName());
        try {
            if (MotanConstants.NODE_TYPE_SERVICE.equals(nodeName)) {
                String traceId = request.getAttachments().get(Tracer.TRACE_ID);
                String spanId = request.getAttachments().get(Tracer.SPAN_ID);
                String parentId = request.getAttachments().get(Tracer.PARENT_ID);
                Tracer.trace(traceId, spanId, parentId);

            } else {
                log.info(LogUtil.marker(request.getArguments()), "调用接口[{}]的方法[{}]", request.getInterfaceName(),
                        request.getMethodName());
                executeBefore(MOTAN_CALL_BEFORE);
                // consumer set trace info to attachment
                String traceId = traceId();
                if (StringUtils.isNotBlank(traceId)) {
                    request.setAttachment(Tracer.TRACE_ID, traceId);
                    request.setAttachment(Tracer.SPAN_ID, spanId());
                    request.setAttachment(Tracer.PARENT_ID, parentId());
                }
            }
            return caller.call(request);
        } finally {
            executeAfter(MOTAN_CALL_AFTER);
        }
    }
}