package com.redick.boot.interceptor;

import com.redick.AroundLogHandler;
import com.redick.aop.proxy.aspectj.AroundLogProxyChainImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

/**
 * @author liupenghui
 * @date 2021/7/7 11:35 下午
 */
@Aspect
@Configuration
@SuppressWarnings("all")
public class LoggerAnnotationInterceptor {

    private final AroundLogHandler aroundLogHandler = new AroundLogHandler();

    @Pointcut("@annotation(com.redick.annotation.LogMarker)")
    public void matchWithLoMarker() {

    }

    /**
     * 执行结果
     * @param joinPoint 切点
     * @return 返回结果
     */
    @Around("matchWithLoMarker()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        return aroundLogHandler.around(new AroundLogProxyChainImpl(joinPoint));
    }
}
