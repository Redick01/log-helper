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

import org.aspectj.lang.Signature;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * JoinPoint Interface.
 * @author Redick01
 */
public interface AroundLogProxyChain {

    /**
     * parameter collections
     * @return key - parameter class name  value - parameter object List
     */
    Map<String, List<Object>> parameter();
    /**
     * get parameters
     * @return parameters
     */
    Object[] getArgs();

    /**
     * get target object.
     * @return target object.
     */
    Object getTarget();

    /**
     * get method.
     * @return Method
     */
    Method getMethod();

    /**
     * target class object.
     * @return class object
     */
    Class<?> getClazz();

    /**
     * exec JoinPoint
     * @return JoinPoint result
     * @throws Throwable 异常
     */
    @SuppressWarnings("all")
    Object getProceed() throws Throwable;

    /**
     * Signature
     * @return Signature.
     */
    Signature getSignature();

    /**
     * exec proxy
     * @param arguments parameter
     * @return exec result
     * @throws Throwable Throwable
     */
    Object doProxyChain(Object[] arguments) throws Throwable;
}
