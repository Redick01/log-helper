package com.redick.proxy;

import com.redick.AroundLogHandler;
import com.redick.proxy.aspectj.AroundLogProxyChainImpl;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 使用aspectj实现aop拦截器
 * @author liu_penghui
 *  2018/10/16.
 */
public class AopInterceptor {

    private final AroundLogHandler aroundLogHandler;

    public AopInterceptor(AroundLogHandler aroundLogHandler) {
        this.aroundLogHandler = aroundLogHandler;
    }

    /**
     * 拦截方法
     * @param joinPoint joinPoint 切点
     * @return object
     * @throws Throwable 异常
     */
    public Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
        return aroundLogHandler.around(new AroundLogProxyChainImpl(joinPoint));
    }

}
