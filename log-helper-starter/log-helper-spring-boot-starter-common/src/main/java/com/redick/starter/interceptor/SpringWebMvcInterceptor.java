package com.redick.starter.interceptor;

import com.redick.common.TraceIdDefine;
import com.redick.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author Redick01
 *  2022/3/25 17:47
 */
@SuppressWarnings("all")
public class SpringWebMvcInterceptor implements HandlerInterceptor {

    @Trace
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = request.getHeader(TraceIdDefine.TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            if (StringUtils.isNotBlank(TraceContext.traceId())) {
                traceId = TraceContext.traceId();
            } else {
                traceId = UUID.randomUUID().toString();
            }
        }
        MDC.put(LogUtil.kLOG_KEY_TRACE_ID, traceId);
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
