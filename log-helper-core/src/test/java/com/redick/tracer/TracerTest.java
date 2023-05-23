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

package com.redick.tracer;

import com.redick.tracer.Tracer.TracerBuilder;
import org.junit.Test;

/**
 * @author Redick01
 */
public class TracerTest {

    @Test
    public void childTest() {
        Tracer tracer = new TracerBuilder()
                .traceId("1112321")
                .build();
        tracer.buildSpan();

        Tracer tracer1 = new TracerBuilder()
                .parentId(tracer.getParentId())
                .traceId(tracer.getTraceId())
                .spanId(tracer.getSpanId())
                .build();
        tracer1.buildSpan();
        Tracer tracer2 = new TracerBuilder()
                .parentId(tracer1.getParentId())
                .traceId(tracer1.getTraceId())
                .spanId(tracer1.getSpanId())
                .build();
        tracer2.buildSpan();
        System.out.println(tracer);
        System.out.println(tracer1);
        System.out.println(tracer2);
    }
}
