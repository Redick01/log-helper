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

package com.redick.support.redis;

import static com.redick.constant.TraceTagConstant.REDIS_EXECUTE_AFTER;
import static com.redick.constant.TraceTagConstant.REDIS_EXECUTE_BEFORE;

import com.redick.support.AbstractInterceptor;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * @author: Redick01
 * @date: 2023/6/26 20:42
 */
@Slf4j
public class SpringRedisCommandInterceptor extends AbstractInterceptor implements MethodInterceptor,
        org.aopalliance.intercept.MethodInterceptor {

    @Nullable
    @Override
    public Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
        return intercept(invocation.getThis(), invocation.getMethod(), invocation.getArguments(), null);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
            throws Throwable {
        String beforeTag = REDIS_EXECUTE_BEFORE;
        String afterTag = REDIS_EXECUTE_AFTER;
        if ("isPipelined".equals(method.getName()) || "close".equals(method.getName())) {
            beforeTag += "_" + method.getName();
            afterTag += "_" + method.getName();
        }
        executeBefore(beforeTag);
        Object result = method.invoke(o, objects);
        executeAfter(afterTag);
        return result;
    }
}
