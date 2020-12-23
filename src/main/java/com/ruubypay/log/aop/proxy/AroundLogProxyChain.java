package com.ruubypay.log.aop.proxy;

import org.aspectj.lang.Signature;

import java.lang.reflect.Method;

/**
 * 获取切面信息接口
 * @Author liu_penghui
 * @Date 2018/10/16.
 */
public interface AroundLogProxyChain {
    /**
     * 获取参数
     * @return 参数
     */
    Object[] getArgs();

    /**
     * 获取切点所在的目标对象
     * @return
     */
    Object getTarget();

    /**
     * 获取方法
     * @return Method
     */
    Method getMethod();

    /**
     * 获取目标Class
     * @return
     */
    Class getClazz();

    /**
     * 获取切点
     * @return
     * @throws Throwable
     */
    Object getProceed() throws Throwable;

    /**
     * 获取切点方法签名对象
     * @return
     */
    Signature getSignature();

    /**
     * 执行方法
     * @param arguments 参数
     * @return 执行结果
     * @throws Throwable Throwable
     */
    Object doProxyChain(Object[] arguments) throws Throwable;
}
