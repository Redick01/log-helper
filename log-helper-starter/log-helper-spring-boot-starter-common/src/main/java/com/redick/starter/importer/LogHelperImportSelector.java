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

package com.redick.starter.importer;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Redick01
 *  2022/3/25 14:29
 */
public class LogHelperImportSelector implements ImportSelector {

    @Override
    @SuppressWarnings("all")
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return Lists.newArrayList("com.redick.starter.configure.LogHelperAutoConfiguration",
                "com.redick.starter.aop.LogHelperAspectJ")
                .toArray(new String[0]);
    }
}
