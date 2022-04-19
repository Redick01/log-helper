package com.redick.starter.aop;

import com.redick.AroundLogHandler;
import com.redick.proxy.aspectj.AroundLogProxyChainImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author Redick01
 *  2022/3/25 14:36
 */
@Aspect
@Configuration
public class LogHelperAspectJ {

    @Resource
    private AroundLogHandler aroundLogHandler;

    @Pointcut("@annotation(com.redick.annotation.LogMarker)")
    public void pointcut() {

    }

    /**
     * 执行结果
     * @param joinPoint 切点
     * @return 返回结果
     * @throws Throwable 异常
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        return aroundLogHandler.around(new AroundLogProxyChainImpl(joinPoint));
    }
}
