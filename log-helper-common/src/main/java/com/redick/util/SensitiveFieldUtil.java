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

package com.redick.util;

import com.redick.annotation.Sensitive;
import java.lang.reflect.Field;

/**
 * @author liu_penghui
 *  2019/5/27.
 */
public class SensitiveFieldUtil {

    /**
     * 参数脱敏
     * @param field Field
     * @param obj parameter
     * @return sensitive parameter
     */
    public static Object getSensitiveArgument(final Field field, Object obj) {
        // 注解实体不为空代表可能需要相应的脱敏操作
        Sensitive sensitive = field.getAnnotation(Sensitive.class);
        if (null != sensitive) {
            // 脱敏
            if (null != obj) {
                int len = obj.toString().length();
                int start = sensitive.start();
                int end = sensitive.end();
                if (start <= 0 || end <= 0 || start >= len - 1 || end >= len - 1) {
                    return obj;
                }
                obj = strReplace(obj.toString(), start, end);
            }
        }
        return obj;
    }

    /**
     * 字符串替换
     *
     * @param str string to replace
     * @param start start index
     * @param end end index
     * @return string
     */
    private static String strReplace(final String str, int start, int end) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i >= start && i <= end) {
                chars[i] = '*';
            }
        }
        return new String(chars);
    }
}
