package com.redick.util;

import com.redick.annotation.FieldIgnore;
import com.redick.annotation.Sensitive;
import net.logstash.logback.marker.LogstashMarker;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.MDC;

import java.lang.reflect.Field;
import java.util.HashMap;

import static net.logstash.logback.marker.Markers.append;

/**
 * 打印日志模版工具类
 * @author liu_penghui
 * @date 2018/10/15.
 */
public class LogUtil {
    public static final String kLOG_KEY_TYPE = "log_pos";
    public static final String kLOG_KEY_DATA = "data";
    public static final String kLOG_KEY_DURATION = "duration";
    public static final String kLOG_KEY_GLOBAL_SESSION_ID_KEY = "traceId";
    public static final String kLOG_KEY_REQUEST_TYPE = "request_type";
    public static final String kLOG_KEY_RESULT = "result";
    public static final String kTYPE_BEGIN = "开始处理";
    public static final String kTYPE_DONE = "处理完毕";
    public static final String kTYPE_FUNC_START = "调用第三方开始";
    public static final String kTYPE_FUNC_END = "调用第三方结束";
    public static final String kTYPE_BIZ = "业务状态变更";
    public static final String kTYPE_BRANCH = "分支";
    public static final String kTYPE_PROCESSING = "过程";
    public static final String kTYPE_EXCEPTION = "异常";
    public static final String kRESULT_SUCCESS = "成功";
    public static final String kRESULT_FAILED = "失败";
    public static final String kTYPE_SENSITIVE = "脱敏处理";

    private static final LogstashMarker DEFAULT_MARKER = append(kLOG_KEY_TYPE, kTYPE_PROCESSING);
    private static final LogstashMarker EXCEPTION_MARKER = append(kLOG_KEY_TYPE, kTYPE_EXCEPTION);
    /**
     * 打印日志模版
     * @param type 类型
     * @param data 数据
     * @return
     */
    public static LogstashMarker marker(String type, Object data) {
        LogstashMarker result = append(kLOG_KEY_TYPE, type);
        if (data != null) {
            result.and(append(kLOG_KEY_DATA, data));
        }
        return result;
    }
    /**
     * 接口处理成功日志打印模版
     * @param data
     * @param duration
     * @return
     */
    public static LogstashMarker processSuccessDoneMarker(Object data, long duration) {
        LogstashMarker result = marker(kTYPE_DONE, data).and(append(kLOG_KEY_DURATION, duration));

        result.and(append(kLOG_KEY_RESULT, kRESULT_SUCCESS));

        return result;
    }
    /**
     * 接口处理失败日志打印模版
     * @param data
     * @param duration
     * @return
     */
    public static LogstashMarker processFailedDoneMarker(Object data, long duration) {
        LogstashMarker result = marker(kTYPE_DONE, data).and(append(kLOG_KEY_DURATION, duration));

        result.and(append(kLOG_KEY_RESULT, kRESULT_FAILED));

        return result;
    }
    /**
     * 处理完毕日志打印模版
     * @param duration
     * @return
     */
    public static LogstashMarker processDoneMarker(long duration) {
        LogstashMarker result = marker(kTYPE_DONE).and(append(kLOG_KEY_DURATION, duration));
        return result;
    }
    /**
     * 接口过程开始日志打印模版
     * @param data
     * @return
     */
    public static LogstashMarker processBeginMarker(Object data) {
        return marker(kTYPE_BEGIN, data);
    }
    /**
     * 接口执行过程日志打印模版
     * @param data
     * @return
     */
    public static LogstashMarker marker(Object data) {
        return marker(kTYPE_PROCESSING, data);
    }
    /**
     * 开始调用第三方接口日志打印模版
     * @param data
     * @return
     */
    public static LogstashMarker funcStartMarker(Object data) {
        return marker(kTYPE_FUNC_START, data);
    }
    /**
     * 结束调用第三方接口日志打印模版
     * @param data
     * @return
     */
    public static LogstashMarker funcEndMarker(Object data) {
        return marker(kTYPE_FUNC_END, data);
    }
    /**
     * 业务状态变更日志打印模版
     * @param data
     * @return
     */
    public static LogstashMarker bizMarker(Object data) {
        return marker(kTYPE_BIZ, data);
    }
    /**
     * 分之日志打印模版
     * @param data
     * @return
     */
    public static LogstashMarker branchMarker(Object data) {
        return marker(kTYPE_BRANCH, data);
    }
    /**
     * 打印日志默认模版
     * @return
     */
    public static LogstashMarker marker() {
        return DEFAULT_MARKER;
    }
    /**
     * 异常日志打印模版
     * @return
     */
    public static LogstashMarker exceptionMarker() {
        return EXCEPTION_MARKER;
    }
    /**
     * 需要自定义request_type打印日志时调用
     * @param processDescription 过程描述（文字描述）
     * @param data 打印数据（可以为null）
     * @param type 日志类型 （如：kTYPE_BEGIN）
     * @return
     */
    public static LogstashMarker requestTypeMarker(String processDescription, Object data, String type) {
        LogstashMarker result = marker(type, data);
        String businessDescription = MDC.get(kLOG_KEY_REQUEST_TYPE);
        result.and(append(kLOG_KEY_REQUEST_TYPE, businessDescription + "." + processDescription));
        return result;
    }
    /**
     * 打印脱敏数据日志模版--支持传入参数类型为java bean，且java bean中不存在泛型参数
     * @param data
     * @return
     */
    public static LogstashMarker commonSensitiveMarker(Object data) {
        if (data == null) {
            return marker();
        }
        Field[] fields = FieldUtils.getAllFields(data.getClass());
        HashMap<String, Object> map = new HashMap<>(fields.length);
        for(Field field : fields){
            String name = "";
            Object argument = null;
            try {
                field.setAccessible(true);
                name = field.getName();
                argument = field.get(data);
                if (null != field.getAnnotation(FieldIgnore.class)) {
                    continue;
                }
                if (null != field.getAnnotation(Sensitive.class)) {
                    argument = SensitiveFieldUtil.getSensitiveArgument(field, argument);
                }
                map.put(name, argument);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                map.put(name, argument);
            }
        }
        return marker(kTYPE_SENSITIVE, map);
    }

    /**
     * 打印脱敏数据日志模版--支持传入参数类型为java bean，且java bean中存在需要脱敏的泛型参数
     * 例如：ModelsReturn中的resData参数
     * @param data
     * @return
     */
    public static LogstashMarker genericSensitiveMarker(Object data) {
        if (data == null) {
            return marker();
        }
        Field[] fields = FieldUtils.getAllFields(data.getClass());
        HashMap<String, Object> map = new HashMap<>(fields.length);
        for(Field field : fields){
            String name = "";
            Object argument = null;
            try {
                field.setAccessible(true);
                name = field.getName();
                argument = field.get(data);
                if (null != field.getAnnotation(FieldIgnore.class)) {
                    continue;
                }
                if (null != field.getAnnotation(Sensitive.class)) {
                    Field[] fields1 = FieldUtils.getAllFields(field.get(data).getClass());
                    HashMap<String, Object> result = new HashMap<>(fields1.length);
                    for (Field field1 : fields1) {
                        field1.setAccessible(true);
                        String name1 = field1.getName();
                        Object argument1 = field1.get(field.get(data));
                        // 如果存在FieldOperate注解则不打印该字段内容
                        if (null != field1.getAnnotation(FieldIgnore.class)) {
                            continue;
                        }
                        argument1 = SensitiveFieldUtil.getSensitiveArgument(field1, argument1);
                        result.put(name1, argument1);
                    }
                    argument = result;
                }
                map.put(name, argument);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                map.put(name, argument);
            }
        }
        return marker(kTYPE_SENSITIVE, map);
    }
}
