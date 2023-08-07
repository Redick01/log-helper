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
 * 2022/3/22 19:30
 */
@Slf4j
public final class ReflectHandler {

    private final Set<String> spiName = Sets.newHashSet();


    private ReflectHandler() {
        spiName.add("java.util.Map");
        spiName.add("java.util.List");
        spiName.add("java.util.Set");
        spiName.add("javax.servlet.http.HttpServletRequest");
    }

    /**
     * 请求参数解析
     *
     * @param chain chain
     * @return 请求参数
     */
    public Object getRequestParameter(final AroundLogProxyChain chain) {
        List<Object> result = Lists.newArrayList();
        chain.parameter().forEach((k, v) -> v.forEach(o -> {
            try {
                result.add(ExtensionLoader
                        .getExtensionLoader(Reflect.class)
                        .getJoin(spiName.contains(k) ? k : "default").reflect(o));
            } catch (UnsupportedEncodingException e) {
                log.error(LogUtil.exceptionMarker(), "UnsupportedEncodingException", e);
            }
        }));
        return result;
    }

    /**
     * 响应参数解析
     *
     * @param o object
     * @return 响应参数
     */
    public Object getResponseParameter(final Object o) {
        Object result = null;
        try {
            result = ExtensionLoader.getExtensionLoader(Reflect.class)
                    .getJoin(spiName.contains(o.getClass().getName()) ? o.getClass().getName() : "default")
                    .reflect(o);
        } catch (UnsupportedEncodingException e) {
            log.error(LogUtil.exceptionMarker(), "UnsupportedEncodingException", e);
        }
        return result;
    }

    public static ReflectHandler getInstance() {
        return ReflectHandlerHolder.INSTANCE;
    }

    /**
     * reflect handler holder
     */
    private static class ReflectHandlerHolder {

        private static final ReflectHandler INSTANCE = new ReflectHandler();
    }
}
