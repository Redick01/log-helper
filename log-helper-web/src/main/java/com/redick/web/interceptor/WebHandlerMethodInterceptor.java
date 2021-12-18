package com.redick.web.interceptor;

import com.redick.common.GlobalSessionIdDefine;
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
 * @author liupenghui
 * @date 2021/6/25 3:41 下午
 */
public class WebHandlerMethodInterceptor implements HandlerInterceptor {

    @Trace
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sessionId = request.getHeader(GlobalSessionIdDefine.GLOBAL_SESSION_ID_KEY);
        if (StringUtils.isBlank(sessionId)) {
            if (StringUtils.isNotBlank(TraceContext.traceId())) {
                sessionId = TraceContext.traceId();
            } else {
                sessionId = UUID.randomUUID().toString();
            }
        }
        response.addHeader(GlobalSessionIdDefine.GLOBAL_SESSION_ID_KEY, sessionId);
        MDC.put(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY, sessionId);
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
