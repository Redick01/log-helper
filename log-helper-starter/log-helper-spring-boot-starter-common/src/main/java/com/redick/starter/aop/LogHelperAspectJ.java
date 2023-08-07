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

package com.redick.starter.aop;

import com.redick.AroundLogHandler;
import com.redick.proxy.aspectj.AroundLogProxyChainImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author Redick01
 *  2022/3/25 14:36
 */
@Aspect
@Configuration
public class LogHelperAspectJ {

    @Resource
    private AroundLogHandler aroundLogHandler;

    /**
     * 切点
     */
    @Pointcut("@annotation(com.redick.annotation.LogMarker)")
    public void pointcut() {

    }

    /**
     * 执行结果
     * @param joinPoint 切点
     * @return 返回结果
     * @throws Throwable 异常
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        return aroundLogHandler.around(new AroundLogProxyChainImpl(joinPoint));
    }
}
