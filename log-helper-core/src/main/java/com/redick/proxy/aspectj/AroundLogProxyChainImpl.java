package com.redick.proxy.aspectj;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.redick.proxy.AroundLogProxyChain;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author liu_penghui
 * @date 2018/10/16.
 */
public class AroundLogProxyChainImpl implements AroundLogProxyChain {

    /**
     * 切点
     */
    private final ProceedingJoinPoint proceedingJoinPoint;
    /**
     * 方法
     */
    private Method method;

    public AroundLogProxyChainImpl(ProceedingJoinPoint joinPoint) {
        this.proceedingJoinPoint = joinPoint;
    }

    /**
     * 获取切点所在的目标对象
     *
     * @return
     */
    @Override
    public Object[] getArgs() {
        return proceedingJoinPoint.getArgs();
    }

    /**
     * 实例
     *
     * @return
     */
    @Override
    public Object getTarget() {
        return proceedingJoinPoint.getTarget();
    }

    /**
     * 获取拦截的方法
     *
     * @return 获取拦截的方法
     */
    @Override
    public Method getMethod() {
        if (null == method) {
            Signature signature = proceedingJoinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            this.method = methodSignature.getMethod();
        }
        return method;
    }

    /**
     * 获取目标Class
     *
     * @return
     */
    @Override
    public Class<?> getClazz() {
        return proceedingJoinPoint.getSignature().getDeclaringType();
    }

    /**
     * 获取切点
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object getProceed() throws Throwable {
        return proceedingJoinPoint.proceed();
    }

    /**
     * 获取切点方法签名对象
     *
     * @return
     */
    @Override
    public Signature getSignature() {
        return proceedingJoinPoint.getSignature();
    }

    @Override
    public Object doProxyChain(Object[] arguments) throws Throwable {
        return proceedingJoinPoint.proceed(arguments);
    }

    @Override
    public Map<String, List<Object>> parameter() {
        Map<String, List<Object>> map = Maps.newConcurrentMap();
        Object[] objects = this.getArgs();
        if (!Objects.isNull(objects)) {
            List<Object> objectList = Arrays.stream(objects).collect(Collectors.toList());
            objectList.forEach(o -> {
                if (!Objects.isNull(o)){
                    List<Object> list = map.getOrDefault(o.getClass().getName(), Lists.newArrayList());
                    list.add(o);
                    map.put(o.getClass().getName(), list);
                }
            });
        }
        return map;
    }
}
