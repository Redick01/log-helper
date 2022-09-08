package com.redick.support.okhttp

import com.redick.support.okhttp.Slf4j.Companion.log
import com.redick.tracer.Tracer
import com.redick.util.LogUtil
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
        return chain.proceed(request)
    }
}