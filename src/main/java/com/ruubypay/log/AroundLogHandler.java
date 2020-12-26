package com.ruubypay.log;

import com.ruubypay.common.dubbo.filter.GlobalSessionIdDefine;
import com.ruubypay.log.annotation.FieldIgnore;
import com.ruubypay.log.annotation.Sensitive;
import com.ruubypay.log.aop.proxy.AroundLogProxyChain;
import com.ruubypay.log.common.ParameterType;
import com.ruubypay.log.resolver.LogEntryNameResolver;
import com.ruubypay.log.util.LogUtil;
import com.ruubypay.log.util.RealLoggerPathUtil;
import com.ruubypay.log.util.SensitiveFieldUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.MDC;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liu_penghui
 * @date 2018/10/16.
 */
public class AroundLogHandler {

    /**
     * 环绕通知
     * @param aroundLogProxyChain 切点
     * @return
     * @throws Throwable
     */
    public Object around(final AroundLogProxyChain aroundLogProxyChain) throws Throwable {
        Logger logger = RealLoggerPathUtil.getRealLogger(aroundLogProxyChain);
        MDC.put(LogUtil.kLOG_KEY_REQUEST_TYPE, LogEntryNameResolver.getBusinessDescription(aroundLogProxyChain));
        if (StringUtils.isEmpty(MDC.get(GlobalSessionIdDefine.kGLOBAL_SESSION_ID_KEY))) {
            MDC.put(GlobalSessionIdDefine.kGLOBAL_SESSION_ID_KEY, UUID.randomUUID().toString());
        }
        String parameterType = getParameterType(aroundLogProxyChain);
        switch (parameterType) {
            case ParameterType.MAP :
            case ParameterType.SET :
            case ParameterType.LIST :
                logger.info(LogUtil.processBeginMarker(getParamHashMapForMap(aroundLogProxyChain)), "开始处理");
                break;
            case ParameterType.HTTP_SERVLET_REQUEST :
                logger.info(LogUtil.processBeginMarker(getParamHashMapHttpServletRequest(aroundLogProxyChain)), "开始处理");
                break;
            default :
                logger.info(LogUtil.processBeginMarker(getParamHashMapForObject(aroundLogProxyChain)), "开始处理");
        }
        long start = System.currentTimeMillis();
        Object o = null;
        try {
            o = aroundLogProxyChain.getProceed();
        } catch (Throwable throwable) {
            logger.error(LogUtil.exceptionMarker(), LogUtil.kTYPE_EXCEPTION, throwable);
        } finally {
            long end = System.currentTimeMillis();
            long elapsedTime = end - start;
            if (o instanceof Map || o instanceof List || o instanceof Set) {
                logger.info(LogUtil.processSuccessDoneMarker(o == null ? o : getParamReturnForCollection(o), elapsedTime), "处理完毕");
            } else {
                logger.info(LogUtil.processSuccessDoneMarker(o == null ? o : getParamHashMapReturnForObject(o), elapsedTime), "处理完毕");
            }
            MDC.clear();
        }
        return o;
    }

    /**
     * 处理java bean类型返回参数
     * @param object
     * @return
     */
    private static HashMap<String, Object> getParamHashMapReturnForObject(final Object object) {
        if (null == object) {
            return null;
        }
        Class c = object.getClass();
        // 取得所有类成员变量
        Field[] fields = FieldUtils.getAllFields(c);
        HashMap<String, Object> map = new HashMap<>(fields.length);
        for(Field field : fields){
            String name = "";
            Object argument = null;
                try {
                    // 取消每个属性的安全检查
                    field.setAccessible(true);
                    name = field.getName();
                    argument = field.get(object);
                    if (null != field.getAnnotation(FieldIgnore.class)) {
                        continue;
                    }
                    if (null !=field.getAnnotation(Sensitive.class)) {
                        if (null != field.get(object)) {
                            Field[] fields1 = FieldUtils.getAllFields(field.get(object).getClass());
                            HashMap<String, Object> result = new HashMap<>(fields1.length);
                            for (Field field1 : fields1) {
                                field1.setAccessible(true);
                                try {
                                    String name1 = field1.getName();
                                    Object argument1 = field1.get(field.get(object));
                                    // 如果存在FieldOperate注解则不打印该字段内容
                                    if (null != field1.getAnnotation(FieldIgnore.class)) {
                                        continue;
                                    }
                                    argument1 = SensitiveFieldUtil.getSensitiveArgument(field1, argument1);
                                    result.put(name1, argument1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            argument = result;
                        }
                    }
                    map.put(name, argument);
                } catch (Exception e) {
                    e.printStackTrace();
                    map.put(name, argument);
                }
        }
        return map;
    }

    /**
     * 处理集合类型返回参数
     * @param obj
     * @return
     */
    private static String getParamReturnForCollection(final Object obj) {
        // 脱敏后的参数
        return SensitiveDataConverter.invokeMsg(obj.toString());
    }

    /**
     * 获取请求参数 参数类型为实体类
     * @param aroundLogProxyChain 切点
     * @return
     */
    private static HashMap<String, Object> getParamHashMapForObject(final AroundLogProxyChain aroundLogProxyChain) {
        CodeSignature codeSignature = (CodeSignature) aroundLogProxyChain.getSignature();
        String[] names = codeSignature.getParameterNames();
        HashMap<String, Object> result = new HashMap<>(names.length);
        if (names == null || names.length == 0) {
            return null;
        }
        Object[] args = aroundLogProxyChain.getArgs();
        if (args == null) {
            return null;
        }
        for (int i = 0; i < names.length; i++) {
            Field[] fields = FieldUtils.getAllFields(args[i].getClass());
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object argument = field.get(args[i]);
                    String name = field.getName();
                    // 如果FieldOperate注解ignore值为true则不打印该字段内容
                    if (null != field.getAnnotation(FieldIgnore.class)) {
                        continue;
                    }
                    argument = SensitiveFieldUtil.getSensitiveArgument(field, argument);
                    // 将处理后的请求参数放到map中
                    result.put(name, argument);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 获取请求参数 参数类型为Map
     * @param aroundLogProxyChain 切点
     * @return
     */
    private static String getParamHashMapForMap(final AroundLogProxyChain aroundLogProxyChain) throws Exception {
        Object[] args = aroundLogProxyChain.getArgs();
        // 脱敏后的参数
        return SensitiveDataConverter.invokeMsg(args[0].toString());
    }

    /**
     * 获取请求参数 参数类型为HttpServletRequest
     * @param aroundLogProxyChain
     * @return
     */
    private static Map<String, String[]> getParamHashMapHttpServletRequest(final AroundLogProxyChain aroundLogProxyChain) throws Exception {
        Object[] args = aroundLogProxyChain.getArgs();
        Map<String, String[]> map = new ConcurrentHashMap<>();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                ((HttpServletRequest) arg).setCharacterEncoding("UTF-8");
                Enumeration<String> names = ((HttpServletRequest) arg).getParameterNames();
                while (names.hasMoreElements()) {
                    String name = names.nextElement();
                    String [] value = ((HttpServletRequest) arg).getParameterValues(name);
                    map.put(name, value);
                }
                return map;
            }
        }
        return null;
    }

    /**
     * 获取参数类型，用于打印不同类型的请求参数
     * @param aroundLogProxyChain
     * @return
     */
    private static String getParameterType(final AroundLogProxyChain aroundLogProxyChain) {
        Method method = aroundLogProxyChain.getMethod();
        Class[] clazz = method.getParameterTypes();
        return clazz.length < 1 ? "" : clazz[0].getName();
    }
}
