package com.redick.example.consumer;

import com.redick.annotation.LogMarker;
import com.redick.example.api.HelloApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Redick01
 * @date 2022/3/29 00:14
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
