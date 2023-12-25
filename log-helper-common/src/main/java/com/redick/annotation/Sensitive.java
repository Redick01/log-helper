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

package com.redick.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liu_penghui
 *  2018/10/23.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
@SuppressWarnings("all")
public @interface Sensitive {

    /**
     * 脱敏数据 左边界
     *
     * 如：left=3
     * 数据1234567890从左边界index=3，从数据4开始脱敏
     *
     * @return 左边界
     */
    int left() default -1;

    /**
     * 脱敏数据 右边界
     *
     * 如：right=3
     * 数据1234567890从右边界index=6，到数据7结束脱敏
     * @return 右边界
     */
    int right() default -1;
}
