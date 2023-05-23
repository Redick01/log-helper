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

package com.redick.support.spring.handler;


import com.redick.support.spring.parser.LogMarkerHandlerParser;
import com.redick.support.spring.parser.LogMarkerInterceptorHandlerParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author Redick01
 *  2018/10/17.
 */
public class LogMarkerNameSpaceHandler extends NamespaceHandlerSupport {
    /**
     * spring配置文件的标签打印通用日志实现类
     */
    private static final String TAG = "handler";
    /**
     * 解析interceptor标签
     */
    private static final String INTERCEPTOR = "interceptor";

    @Override
    public void init() {
        registerBeanDefinitionParser(TAG, new LogMarkerHandlerParser());
        registerBeanDefinitionParser(INTERCEPTOR, new LogMarkerInterceptorHandlerParser());
    }
}
