/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redick.util;

import static com.redick.constant.TraceTagConstant.ENDPOINT_DONE;
import static net.logstash.logback.marker.Markers.append;

import com.redick.annotation.FieldIgnore;
import com.redick.annotation.Sensitive;
import java.lang.reflect.Field;
import java.util.HashMap;
import net.logstash.logback.marker.LogstashMarker;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.MDC;

/**
 * 打印日志模版工具类
 * @author liu_penghui
 *  2018/10/15.
 */
@SuppressWarnings("all")
public class LogUtil {

    public static final String kLOG_KEY_TYPE = "log_pos";
    public static final String kLOG_KEY_DATA = "data";
    public static final String kLOG_KEY_DURATION = "duration";
    public static final String kLOG_KEY_TRACE_ID = "traceId";
    public static final String kLOG_KEY_REQUEST_TYPE = "request_type";
    public static final String kLOG_KEY_INTERFACE_NAME = "interface_name";
    public static final String kLOG_KEY_TRACE_TAG = "trace_tag";
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
     * @return Logstash日支模板
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
     * @param data 数据
     * @param duration 时长
     * @return Logstash日支模板
     */
    public static LogstashMarker processSuccessDoneMarker(Object data, long duration) {
        LogstashMarker result = marker(kTYPE_DONE, data).and(append(kLOG_KEY_DURATION, duration));
        result.and(append(kLOG_KEY_TRACE_TAG, ENDPOINT_DONE));
        result.and(append(kLOG_KEY_RESULT, kRESULT_SUCCESS));
        return result;
    }
    /**
     * 接口处理失败日志打印模版
     * @param data 数据
     * @param duration 时长
     * @return Logstash日支模板
     */
    public static LogstashMarker processFailedDoneMarker(Object data, long duration) {
        LogstashMarker result = marker(kTYPE_DONE, data).and(append(kLOG_KEY_DURATION, duration));
        result.and(append(kLOG_KEY_TRACE_TAG, ENDPOINT_DONE));
        result.and(append(kLOG_KEY_RESULT, kRESULT_FAILED));
        return result;
    }
    /**
     * 处理完毕日志打印模版
     * @param duration 时长
     * @return Logstash日支模板
     */
    public static LogstashMarker processDoneMarker(long duration) {
        LogstashMarker result = marker(kTYPE_DONE).and(append(kLOG_KEY_DURATION, duration));
        return result;
    }
    /**
     * 接口过程开始日志打印模版
     * @param data 数据
     * @return Logstash日支模板
     */
    public static LogstashMarker processBeginMarker(Object data) {
        return marker(kTYPE_BEGIN, data);
    }
    /**
     * 接口执行过程日志打印模版
     * @param data 数据
     * @return Logstash日支模板
     */
    public static LogstashMarker marker(Object data) {
        return marker(kTYPE_PROCESSING, data);
    }
    /**
     * 开始调用第三方接口日志打印模版
     * @param data 数据
     * @return Logstash日支模板
     */
    public static LogstashMarker funcStartMarker(Object data) {
        return marker(kTYPE_FUNC_START, data);
    }
    /**
     * 结束调用第三方接口日志打印模版
     * @param data 数据
     * @return Logstash日支模板
     */
    public static LogstashMarker funcEndMarker(Object data) {
        return marker(kTYPE_FUNC_END, data);
    }
    /**
     * 业务状态变更日志打印模版
     * @param data 数据
     * @return Logstash日支模板
     */
    public static LogstashMarker bizMarker(Object data) {
        return marker(kTYPE_BIZ, data);
    }
    /**
     * 分之日志打印模版
     * @param data 数据
     * @return Logstash日支模板
     */
    public static LogstashMarker branchMarker(Object data) {
        return marker(kTYPE_BRANCH, data);
    }
    /**
     * 打印日志默认模版
     * @return Logstash日支模板
     */
    public static LogstashMarker marker() {
        return DEFAULT_MARKER;
    }
    /**
     * 异常日志打印模版
     * @return Logstash日支模板
     */
    public static LogstashMarker exceptionMarker() {
        return EXCEPTION_MARKER;
    }
    /**
     * 需要自定义request_type打印日志时调用
     * @param processDescription 过程描述（文字描述）
     * @param data 打印数据（可以为null）
     * @param type 日志类型 （如：kTYPE_BEGIN）
     * @return Logstash日支模板
     */
    public static LogstashMarker requestTypeMarker(String processDescription, Object data, String type) {
        LogstashMarker result = marker(type, data);
        String businessDescription = MDC.get(kLOG_KEY_REQUEST_TYPE);
        result.and(append(kLOG_KEY_REQUEST_TYPE, businessDescription + "." + processDescription));
        return result;
    }

    /**
     * 添加自定义字段模板
     * @param fileName 字段名
     * @param data 数据
     * @return LogstashMarker
     */
    public static LogstashMarker responseTimeMarker(String fileName, Object data) {
        return append(fileName, data);
    }

    /**
     * 打印脱敏数据日志模版--支持传入参数类型为java bean，且java bean中不存在泛型参数
     * @param data 数据
     * @return Logstash日支模板
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
     * @param data 数据
     * @return Logstash日支模板
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

    public static LogstashMarker customizeMarker(String fileName, Object data) {
        return append(fileName, data);
    }
}
