package com.redick.reflect;

import com.google.common.collect.Maps;
import com.redick.aop.proxy.AroundLogProxyChain;
import com.redick.common.ParameterType;
import com.redick.reflect.impl.CollectionParameterReflect;
import com.redick.reflect.impl.HttpServletRequestReflect;
import com.redick.reflect.impl.JavaBeanParameterReflect;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Redick01
 * @date 2022/3/22 19:30
 */
public class ReflectHandler {

    private static final Map<String, Reflect> REFLECTS = Maps.newConcurrentMap();

    private ReflectHandler() {
        REFLECTS.put(ParameterType.HTTP_SERVLET_REQUEST, new HttpServletRequestReflect());
        REFLECTS.put(ParameterType.JAVA_BEAN, new JavaBeanParameterReflect());
        REFLECTS.put(ParameterType.LIST, new CollectionParameterReflect());
        REFLECTS.put(ParameterType.SET, new CollectionParameterReflect());
        REFLECTS.put(ParameterType.MAP, new CollectionParameterReflect());
    }

    public Object param(final AroundLogProxyChain chain) throws UnsupportedEncodingException {
        String[] parameterTypes = getParameterType(chain);
        Map<String, Object> map = new HashMap<>(parameterTypes.length);
        for (String paramType : parameterTypes) {

            Object ob = REFLECTS.get(paramType).reflect(chain);
            map.put(ob.getClass().getName(), ob);
        }
        return map;
    }

    /**
     * 获取参数类型
     * @param aroundLogProxyChain 切面
     * @return 参数类型数组
     */
    public static String[] getParameterType(final AroundLogProxyChain aroundLogProxyChain) {
        Method method = aroundLogProxyChain.getMethod();
        Class[] clazzs = method.getParameterTypes();
        String[] paramTypes = new String[clazzs.length];
        if (clazzs.length < 1) {
            return paramTypes;
        }
        for (int i = 0; i < clazzs.length; i++) {
            paramTypes[i] = clazzs[i].getName();
        }
        return paramTypes;
    }

    public static ReflectHandler getInstance() {
        return ReflectHandlerHolder.INSTANCE;
    }

    private static class ReflectHandlerHolder {

        private static final ReflectHandler INSTANCE = new ReflectHandler();
    }
}
