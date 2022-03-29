package com.redick.loghelper.controller;

import com.redick.annotation.LogMarker;
import com.redick.executor.TtlThreadPoolExecutor;
import com.redick.executor.TtlThreadPoolTaskExecutor;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Redick01
 * @date 2022/3/29 17:06
 */
@RestController
@Slf4j
public class TestController {

    @Resource(name = "ttlThreadPoolTaskExecutor")
    private TtlThreadPoolTaskExecutor ttlThreadPoolTaskExecutor;

    @Resource(name = "ttlThreadPoolExecutor")
    private TtlThreadPoolExecutor ttlThreadPoolExecutor;

    @LogMarker(businessDescription = "say方法", interfaceName = "com.redick.loghelper.controller.TestController#say()")
    @GetMapping("/test")
    public String say(String content) {

        ttlThreadPoolExecutor.execute(() -> {
            log.info(LogUtil.marker("ttlThreadPoolExecutor"), content);
        });

        ttlThreadPoolTaskExecutor.execute(() -> {
            log.info(LogUtil.marker("ttlThreadPoolTaskExecutor"), content);
        });

        return "say" + content;
    }
}
