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
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author: Redick01
 * @date: 2023/6/26 21:09
 */
public class SpringRedisConnectionFactoryPostProcessor implements BeanPostProcessor {

    private static final String GET_CONNECTION = "getConnection";

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName)
            throws BeansException {
        if (bean instanceof RedisConnectionFactory) {
            return ProxyUtil.getProxy(bean, this::interceptorRedisFactory);
        }
        return bean;
    }

    public Object interceptorRedisFactory(final MethodInvocation methodInvocation)
            throws Throwable {
        Object result = methodInvocation.proceed();
        if (GET_CONNECTION.equals(methodInvocation.getMethod().getName())) {
            return ProxyUtil.getProxy(result, new SpringRedisCommandInterceptor());
        }
        return result;
    }
}
