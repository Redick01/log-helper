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

package com.redick.support.spring.parser;

import com.redick.proxy.AopInterceptor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * @author Redick01
 *  2018/10/17.
 */
@SuppressWarnings("all")
public class LogMarkerInterceptorHandlerParser extends AbstractSingleBeanDefinitionParser {

    /**
     * handler引用
     */
    private static final String HANDLER_REF="handler";

    @Override
    protected Class<?> getBeanClass(final Element element) {
        return AopInterceptor.class;
    }

    @Override
    protected void doParse(final Element element, final BeanDefinitionBuilder builder) {
        String handlerBean = element.getAttribute(HANDLER_REF);
        builder.addConstructorArgReference(handlerBean);
    }
}
