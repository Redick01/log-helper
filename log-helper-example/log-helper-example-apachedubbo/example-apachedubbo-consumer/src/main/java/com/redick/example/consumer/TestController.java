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

package com.redick.example.consumer;

import com.redick.annotation.LogMarker;
import com.redick.example.api.HelloApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Redick01
 *  2022/3/29 00:14
 */
@RestController
public class TestController {

    @DubboReference(interfaceClass = HelloApi.class, version = "1.0.0" /*,
            methods = {
                    @Method(
                            name = "sayHello",
                            oninvoke = "notify.oninvoke",
                            onreturn = "notify.onreturn",
                            onthrow = "notify.onthrow")
            }
             */
    )
    private HelloApi helloApi;

    @GetMapping("/test")
    @LogMarker(businessDescription = "消费say接口", interfaceName = "com.redick.example.consumer#test()")
    public String test() {
        return helloApi.say("hhh");
    }
}
