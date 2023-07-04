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

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;

/**
 * @author: Redick01
 * @date: 2023/6/29 14:35
 */
public class ProxyUtil {

    public static Object getProxy(final Object object, final MethodInterceptor methodInterceptor) {
        ProxyFactory proxyFactory = new ProxyFactory(object);
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvice(methodInterceptor);
        return proxyFactory.getProxy();
    }
}
