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

package com.redick.support.okhttp3

import com.redick.constant.TraceTagConstant.OKHTTP_CLIENT_EXEC_AFTER
import com.redick.constant.TraceTagConstant.OKHTTP_CLIENT_EXEC_BEFORE
import com.redick.support.okhttp3.Slf4j.Companion.log
import com.redick.tracer.Tracer
import com.redick.util.LogUtil
import net.logstash.logback.marker.Markers.append
import okhttp3.Interceptor
import okhttp3.Response
import org.apache.commons.lang3.StringUtils
import org.slf4j.MDC

/**
 * @author Redick01
 *  2022/3/31 18:36
 */
@Slf4j
class TraceIdOkhttp3Interceptor : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        try {
            val builder = request.newBuilder()
            val traceId = MDC.get(Tracer.TRACE_ID)
            if (StringUtils.isNotBlank(traceId)) {
                builder.addHeader(Tracer.TRACE_ID, traceId)
            }
            val spanId = MDC.get(Tracer.SPAN_ID)
            if (StringUtils.isNotBlank(spanId)) {
                builder.addHeader(Tracer.SPAN_ID, spanId)
            }
            val parentId = MDC.get(Tracer.PARENT_ID)
            if (StringUtils.isNotBlank(parentId)) {
                builder.addHeader(Tracer.PARENT_ID, parentId)
            }
            request = builder.build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        log.info(LogUtil.customizeMarker(LogUtil.kLOG_KEY_TRACE_TAG, OKHTTP_CLIENT_EXEC_BEFORE), OKHTTP_CLIENT_EXEC_BEFORE)
        val startTime = System.currentTimeMillis();
        val response = chain.proceed(request)
        val duration = System.currentTimeMillis() - startTime;
        log.info(LogUtil.customizeMarker(LogUtil.kLOG_KEY_TRACE_TAG, OKHTTP_CLIENT_EXEC_AFTER)
                .and(append(LogUtil.kLOG_KEY_DURATION, duration))
                , OKHTTP_CLIENT_EXEC_AFTER);
        return response
    }
}