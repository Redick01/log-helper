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
