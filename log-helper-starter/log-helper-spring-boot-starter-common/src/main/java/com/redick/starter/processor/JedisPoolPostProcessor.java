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

package com.redick.starter.processor;

import com.redick.support.redis.SpringRedisCommandInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import redis.clients.jedis.JedisPool;

/**
 * @author: Redick01
 * @date: 2023/6/29 14:34
 */
public class JedisPoolPostProcessor implements BeanPostProcessor {

    private static final String GET_RESOURCE = "getResource";

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof JedisPool) {
            return ProxyUtil.getProxy(bean, this::interceptorJedisPool);
        }
        return bean;
    }

    /**
     * 方法拦截
     *
     * @param methodInvocation 方法拦截
     * @return result
     * @throws Throwable Throwable
     */
    public Object interceptorJedisPool(final MethodInvocation methodInvocation)
            throws Throwable {
        Object result = methodInvocation.proceed();
        if (GET_RESOURCE.equals(methodInvocation.getMethod().getName())) {
            return ProxyUtil.getProxy(result, new SpringRedisCommandInterceptor());
        }
        return result;
    }
}
