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

import com.redick.annotation.FieldIgnore;
import com.redick.annotation.ValidChild;
import com.redick.reflect.Reflect;
import com.redick.spi.Join;
import com.redick.util.SensitiveFieldUtil;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author Redick01
 *  2022/3/22 19:44
 */
@Join
public class JavaBeanParameterReflect implements Reflect {

    @Override
    public Object reflect(Object obj) throws UnsupportedEncodingException {
        if (null == obj) {
            return null;
        }
        Field[] fields = FieldUtils.getAllFields(obj.getClass());
        HashMap<String, Object> result = new HashMap<>(fields.length);
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object argument = field.get(obj);
                String name = field.getName();
                if (null != field.getAnnotation(ValidChild.class)) {
                    argument = reflect(argument);
                }
                // 如果FieldOperate注解ignore值为true则不打印该字段内容
                if (null != field.getAnnotation(FieldIgnore.class)) {
                    continue;
                }
                argument = SensitiveFieldUtil.getSensitiveArgument(field, argument);
                // 将处理后的请求参数放到map中
                result.put(name, argument);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
