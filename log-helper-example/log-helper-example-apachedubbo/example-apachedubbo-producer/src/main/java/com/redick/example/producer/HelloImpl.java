package com.redick.example.producer;

import com.redick.annotation.LogMarker;
import com.redick.example.api.HelloApi;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

/**
 * @author Redick01
 *  2022/3/28 13:50
 */
@DubboService(version = "1.0.0", methods = {@Method(name = "say", timeout = 120000, retries = 0)})
public class HelloImpl implements HelloApi {

    @Override
    @LogMarker(businessDescription = "say接口", interfaceName = "com.redick.example.producer.HelloImpl#say()")
    public String say(String content) {
        return "say" + content;
    }
}
