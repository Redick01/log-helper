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

package com.redick.spi.test;

import com.redick.spi.ExtensionFactory;
import com.redick.spi.ExtensionLoader;
import com.redick.spi.SpiExtensionFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author liupenghui
 */
public class SpiExtensionFactoryTest {

    @Test
    public void getExtensionTest() {
        TestSPI testSPI = ExtensionLoader.getExtensionLoader(TestSPI.class).getJoin("testSPI2");
        testSPI.test();
    }
}
