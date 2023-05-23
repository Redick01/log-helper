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

package com.redick.reflect;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.redick.proxy.AroundLogProxyChain;
import com.redick.spi.ExtensionLoader;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

/**
 * @author Redick01
 *  2022/3/22 19:30
 */
@Slf4j
public class ReflectHandler {

    private final Set<String> SPI_NAME = Sets.newHashSet();


    private ReflectHandler() {
        SPI_NAME.add("java.util.Map");
        SPI_NAME.add("java.util.List");
        SPI_NAME.add("java.util.Set");
        SPI_NAME.add("javax.servlet.http.HttpServletRequest");
    }

    public Object getRequestParameter(final AroundLogProxyChain chain) {
        List<Object> result = Lists.newArrayList();
        chain.parameter().forEach((k, v) -> v.forEach(o -> {
            try {
                result.add(ExtensionLoader.getExtensionLoader(Reflect.class).getJoin(SPI_NAME.contains(k) ? k : "default").reflect(o));
            } catch (UnsupportedEncodingException e) {
                log.error(LogUtil.exceptionMarker(),"UnsupportedEncodingException", e);
            }
        }));
        return result;
    }

    public Object getResponseParameter(final Object o) {
        Object result = null;
        try {
            result = ExtensionLoader.getExtensionLoader(Reflect.class).getJoin(SPI_NAME.contains(o.getClass().getName()) ? o.getClass().getName() : "default").reflect(o);
        } catch (UnsupportedEncodingException e) {
            log.error(LogUtil.exceptionMarker(),"UnsupportedEncodingException", e);
        }
        return result;
    }

    public static ReflectHandler getInstance() {
        return ReflectHandlerHolder.INSTANCE;
    }

    private static class ReflectHandlerHolder {

        private static final ReflectHandler INSTANCE = new ReflectHandler();
    }
}
