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

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

/**
 * @author: Redick01
 * @date: 2023/9/5 22:51
 */
@Priority(Priorities.USER)
public class DubboxRestTraceFilter extends AbstractInterceptor implements ContainerRequestFilter, ClientRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        MultivaluedMap<String, String> headers = requestContext.getHeaders();
        if (headers.containsKey(Tracer.TRACE_ID)) {
            String traceId = headers.get(Tracer.TRACE_ID).get(0);
            String spanId = null;
            if (null != headers.get(Tracer.SPAN_ID)) {
                spanId = headers.get(Tracer.SPAN_ID).get(0);
            }
            String parentId = null;
            if (null != headers.get(Tracer.PARENT_ID)) {
                parentId = headers.get(Tracer.PARENT_ID).get(0);
            }
            if (StringUtils.isNotBlank(traceId)) {
                mdc(traceId, spanId, parentId);
            }
        }
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        // todo 目前没遇到此场景
    }
}
