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

package com.redick.support.mysql;

import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.log.Log;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Redick01
 */
@Slf4j
public class Mysql8ExceptionInterceptor implements ExceptionInterceptor {

    @Override
    public ExceptionInterceptor init(Properties properties, Log log) {
        String queryInterceptors = properties.getProperty("queryInterceptors");
        if (queryInterceptors == null || !queryInterceptors.contains(Mysql8ExceptionInterceptor.class.getName())) {
            throw new IllegalStateException(
                    "TracingQueryInterceptor must be enabled to use TracingExceptionInterceptor.");
        }
        return new Mysql8ExceptionInterceptor();
    }

    @Override
    public void destroy() {

    }

    @Override
    public Exception interceptException(Exception e) {
        return null;
    }
}
