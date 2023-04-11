package com.redick.support.okhttp3

import com.redick.constant.TraceTagConstant
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
            val traceId = MDC.get(Tracer.TRACE_ID)
            if (StringUtils.isNotBlank(traceId)) {
                request = request
                        .newBuilder()
                        .addHeader(Tracer.TRACE_ID, traceId)
                        .addHeader(Tracer.SPAN_ID, MDC.get(Tracer.SPAN_ID))
                        .addHeader(Tracer.PARENT_ID, MDC.get(Tracer.PARENT_ID))
                        .build()
            } else {
                log.info(LogUtil.marker(), "当前线程没有traceId")
            }
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