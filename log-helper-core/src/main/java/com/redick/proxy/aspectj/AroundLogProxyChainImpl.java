/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 *  2018/10/16.
 */
@SuppressWarnings("all")
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
     * @return Object
     */
    @Override
    public Object[] getArgs() {
        return proceedingJoinPoint.getArgs();
    }

    /**
     * 实例
     *
     * @return Object
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
     * @return 目标Class
     */
    @Override
    public Class<?> getClazz() {
        return proceedingJoinPoint.getSignature().getDeclaringType();
    }

    /**
     * 获取切点
     *
     * @return 切面执行结果
     * @throws Throwable 异常
     */
    @Override
    public Object getProceed() throws Throwable {
        return proceedingJoinPoint.proceed();
    }

    /**
     * 获取切点方法签名对象
     *
     * @return 方法签名
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
