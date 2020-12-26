package com.ruubypay.log.resolver;

import com.ruubypay.log.annotation.LogMarker;
import com.ruubypay.log.aop.proxy.AroundLogProxyChain;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author liu_penghui
 * @date 2018/10/16.
 */
public class LogEntryNameResolver {

    /**
     * 获取接口业务描述（尽量中文描述）
     * @return
     */
    public static String getBusinessDescription(AroundLogProxyChain aroundLogProxyChain) throws ClassNotFoundException {
        Method method = aroundLogProxyChain.getMethod();
        return method.getAnnotation(LogMarker.class).businessDescription();
    }
}
