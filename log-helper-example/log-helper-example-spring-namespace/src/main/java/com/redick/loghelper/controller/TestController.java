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

package com.redick.loghelper.controller;

import com.alibaba.ttl.TtlRunnable;
import com.redick.annotation.LogMarker;
import com.redick.executor.TtlThreadPoolExecutor;
import com.redick.executor.TtlThreadPoolTaskExecutor;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Redick01
 *  2022/3/29 17:06
 */
@RestController
@Slf4j
public class TestController {

    @Resource(name = "ttlThreadPoolTaskExecutor")
    private TtlThreadPoolTaskExecutor ttlThreadPoolTaskExecutor;

    @Resource(name = "ttlThreadPoolExecutor")
    private TtlThreadPoolExecutor ttlThreadPoolExecutor;

    @Resource(name = "threadPoolExecutor")
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * test api
     *
     * @param content request content
     * @return response
     */
    @LogMarker(businessDescription = "say方法", interfaceName = "com.redick.loghelper.controller.TestController#say()")
    @GetMapping("/test")
    public String say(String content) {

        ttlThreadPoolExecutor.execute(() -> {
            log.info(LogUtil.marker("ttlThreadPoolExecutor"), content);
        });

        ttlThreadPoolTaskExecutor.execute(() -> {
            log.info(LogUtil.marker("ttlThreadPoolTaskExecutor"), content);
        });

        threadPoolExecutor.execute(TtlRunnable.get(() -> {
            log.info(LogUtil.marker("threadPoolExecutor"), content);
        }));

        return "say" + content;
    }
}
