package com.ruubypay.log.resolver;

import com.ruubypay.log.annotation.LogMarker;
import com.ruubypay.log.aop.proxy.AroundLogProxyChain;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @Author liu_penghui
 * @Date 2018/10/16.
 */
public class LogEntryNameResolver {
    //把方法签名和实际请求的中文名称对应起来，便于AOP日志记录时使用
    public static HashMap<String, String> map = new HashMap<String, String>() {{
        put("", "");
        put("", "");
        put("", "");
        put("", "");
        put("", "");
        put("", "");
        put("", "");
    }};

    public static String getNameForSignature(String signatureString) {
        return map.containsKey(signatureString) ? map.get(signatureString) : signatureString;
    }

    /**
     * 获取接口业务描述（尽量中文描述）
     * @return
     */
    public static String getBusinessDescription(AroundLogProxyChain aroundLogProxyChain) throws ClassNotFoundException {
        Method method = aroundLogProxyChain.getMethod();
        return method.getAnnotation(LogMarker.class).businessDescription() == null ? "" : method.getAnnotation(LogMarker.class).businessDescription();
    }
}
