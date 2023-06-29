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

package io.redick.lettuce.controller;

import com.redick.annotation.LogMarker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Redick01
 * @date: 2023/6/27 15:10
 */
@RestController
@RequestMapping("/redis-command")
@Slf4j
@AllArgsConstructor
public class TestController {


    private final static String FLAG = "sys_config:sys.account.registerUser";

    @GetMapping("/get")
    @LogMarker(businessDescription = "jedis-command-get")
    public String getParam() {
//        if (jedisPool.getResource().exists(FLAG)) {
//            return FLAG;
//        }
        return "null";
    }
}
