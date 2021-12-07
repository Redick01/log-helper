package com.ruubypay.log.filter.web;

import com.ruubypay.log.common.GlobalSessionIdDefine;
import com.ruubypay.log.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String sessionId = request.getHeader(GlobalSessionIdDefine.kGLOBAL_SESSION_ID_KEY);
            if (StringUtils.isBlank(sessionId)) {
                sessionId = UUID.randomUUID().toString();
            }
            response.addHeader(GlobalSessionIdDefine.kGLOBAL_SESSION_ID_KEY, sessionId);
            MDC.put(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY, sessionId);
        } finally {
            MDC.remove(GlobalSessionIdDefine.kGLOBAL_SESSION_ID_KEY);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(GlobalSessionIdDefine.kGLOBAL_SESSION_ID_KEY);
    }
}
