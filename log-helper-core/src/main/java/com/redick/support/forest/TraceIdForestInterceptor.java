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

package com.redick.support.forest;

import static com.redick.constant.TraceTagConstant.FOREST_EXEC_AFTER;
import static com.redick.constant.TraceTagConstant.FOREST_EXEC_BEFORE;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.redick.constant.TraceIdDefine;
import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Redick01
 */
@Slf4j
public class TraceIdForestInterceptor extends AbstractInterceptor implements Interceptor<Object> {

    @Override
    public boolean beforeExecute(ForestRequest request) {
        if (StringUtils.isNotBlank(traceId())) {
            request.addHeader(TraceIdDefine.TRACE_ID, traceId());
            request.addHeader(Tracer.SPAN_ID, spanId());
            request.addHeader(Tracer.PARENT_ID, parentId());
            super.executeBefore(FOREST_EXEC_BEFORE);
        } else {
            log.info(LogUtil.marker(), "current thread have not the traceId!");
        }
        return true;
    }

    @Override
    public void afterExecute(ForestRequest request, ForestResponse response) {
        super.executeAfter(FOREST_EXEC_AFTER);
        log.info(LogUtil.funcEndMarker(response.getContent()), "forest请求处理完毕");
    }
}
