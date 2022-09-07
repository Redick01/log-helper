package com.redick.starter.interceptor;

import com.redick.tracer.Tracer;
import com.redick.tracer.Tracer.TracerBuilder;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Redick01
 *  2022/3/25 17:47
 */
@SuppressWarnings("all")
public class SpringWebMvcInterceptor implements HandlerInterceptor {

    @Trace
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Tracer tracer = Tracer.traceThreadLocal.get();
        if (null == tracer) {
            String traceId = request.getHeader(Tracer.TRACE_ID);
            String spanId = request.getHeader(Tracer.SPAN_ID);
            String parentId = request.getHeader(Tracer.PARENT_ID);
            tracer = new TracerBuilder()
                    .traceId(traceId)
                    .spanId(null == spanId ? null : Integer.parseInt(spanId))
                    .parentId(null == parentId ? null : Integer.parseInt(parentId))
                    .build();
            tracer.buildSpan();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.clear();
    }
}
