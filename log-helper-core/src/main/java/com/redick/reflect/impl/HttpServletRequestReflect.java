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

package com.redick.reflect.impl;

import com.google.common.collect.Maps;
import com.redick.reflect.Reflect;
import com.redick.spi.Join;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author Redick01
 * 2022/3/22 13:55
 */
@Slf4j
@Join
public class HttpServletRequestReflect implements Reflect {

    @Override
    public Object reflect(Object obj) throws UnsupportedEncodingException {
        Map<String, String[]> result = Maps.newConcurrentMap();
        if (null == obj) {
            return result;
        }
        if (obj instanceof HttpServletRequest) {
            try {
                ((HttpServletRequest) obj).setCharacterEncoding("UTF-8");
                Enumeration<String> names = ((HttpServletRequest) obj).getParameterNames();
                while (names.hasMoreElements()) {
                    String name = names.nextElement();
                    String[] value = ((HttpServletRequest) obj).getParameterValues(name);
                    result.put(name, value);
                }
            } catch (UnsupportedEncodingException e) {
                log.error(LogUtil.exceptionMarker(), "UnsupportedEncodingException", e);
            }
        }
        return result;
    }
}
