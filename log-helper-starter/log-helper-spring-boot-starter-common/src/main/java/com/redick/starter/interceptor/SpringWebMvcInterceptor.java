package com.redick.starter.interceptor;

import com.redick.tracer.Tracer;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = request.getHeader(Tracer.TRACE_ID);
        String parentId = request.getHeader(Tracer.SPAN_ID);
        String spanId = request.getHeader(Tracer.PARENT_ID);
        Tracer.trace(traceId, parentId, spanId);
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
