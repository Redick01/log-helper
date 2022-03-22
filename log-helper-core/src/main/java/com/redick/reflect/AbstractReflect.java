package com.redick.reflect;


import com.redick.annotation.LogMarker;
import com.redick.aop.proxy.AroundLogProxyChain;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Redick01
 * @date 2022/3/22 13:59
 */
public abstract class AbstractReflect implements Reflect {

    @Override
    public Object reflect(AroundLogProxyChain obj) {
        return doReflect(obj);
    }

    public abstract Object doReflect(Object obj);




    /**
     * 获取接口业务描述（尽量中文描述）
     * @return 业务描述
     */
    public String getBusinessDescription(AroundLogProxyChain aroundLogProxyChain) throws ClassNotFoundException {
        Method method = aroundLogProxyChain.getMethod();
        return method.getAnnotation(LogMarker.class).businessDescription();
    }
}
