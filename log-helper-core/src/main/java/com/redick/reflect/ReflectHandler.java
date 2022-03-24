package com.redick.reflect;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.redick.aop.proxy.AroundLogProxyChain;
import com.redick.common.ParameterType;
import com.redick.reflect.impl.CollectionParameterReflect;
import com.redick.reflect.impl.HttpServletRequestReflect;
import com.redick.reflect.impl.JavaBeanParameterReflect;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @author Redick01
 * @date 2022/3/22 19:30
 */
@Slf4j
public class ReflectHandler {

    private static final Map<String, Reflect> REFLECTS = Maps.newConcurrentMap();

    private ReflectHandler() {
        REFLECTS.put(ParameterType.HTTP_SERVLET_REQUEST, new HttpServletRequestReflect());
        REFLECTS.put(ParameterType.JAVA_BEAN, new JavaBeanParameterReflect());
        REFLECTS.put(ParameterType.LIST, new CollectionParameterReflect());
        REFLECTS.put(ParameterType.SET, new CollectionParameterReflect());
        REFLECTS.put(ParameterType.MAP, new CollectionParameterReflect());
    }

    public Object getRequestParameter(final AroundLogProxyChain chain) {
        Map<String, List<Object>> result = Maps.newHashMap();
        chain.parameter().forEach((k, v) -> {
            v.forEach(o -> {
                try {
                    result.getOrDefault(k, Lists.newArrayList()).add(REFLECTS.get(k).reflect(o));
                } catch (UnsupportedEncodingException e) {
                    log.error(LogUtil.exceptionMarker(),"UnsupportedEncodingException", e);
                }
            });
        });
        return result;
    }

    public Object getResponseParameter(final Object o) {
        Object result = null;
        try {
            result = REFLECTS.get(o.getClass().getName()).reflect(o);
        } catch (UnsupportedEncodingException e) {
            log.error(LogUtil.exceptionMarker(),"UnsupportedEncodingException", e);
        }
        return result;
    }

    public static ReflectHandler getInstance() {
        return ReflectHandlerHolder.INSTANCE;
    }

    private static class ReflectHandlerHolder {

        private static final ReflectHandler INSTANCE = new ReflectHandler();
    }
}
