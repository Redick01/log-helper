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

package com.redick.executor;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author Redick01
 */
public class ContextCopyingDecorator implements TaskDecorator {

    @NotNull
    @Override
    public Runnable decorate(@NotNull Runnable runnable) {
        RequestAttributes context = RequestContextHolder.currentRequestAttributes();
        Map<String, String> previous = MDC.getCopyOfContextMap();
        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(context);
                MDC.setContextMap(previous);
                runnable.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
                MDC.clear();
            }
        };
    }
}
