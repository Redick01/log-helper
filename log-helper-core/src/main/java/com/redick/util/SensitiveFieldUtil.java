package com.redick.util;

import com.redick.SensitiveDataConverter;
import com.redick.annotation.FieldIgnore;
import com.redick.annotation.Sensitive;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liu_penghui
 * @date 2019/5/27.
 */
public class SensitiveFieldUtil {

    private SensitiveFieldUtil() {

    }
    /**
     * 参数脱敏
     * @param field
     * @param obj
     * @return
     */
    public static Object getSensitiveArgument(final Field field, Object obj) {
        // 注解实体不为空代表可能需要相应的脱敏操作
        if (null != field.getAnnotation(Sensitive.class)) {
            // 获取注解值
            String paramSensitiveType = field.getAnnotation(Sensitive.class).paramSensitiveType();
            // 获取注解值
            boolean isSensitive = field.getAnnotation(Sensitive.class).isSensitive();
            if (isSensitive) {
                // 脱敏
                if (null != obj) {
                    obj = SensitiveDataConverter.sensitiveConvert(obj.toString(), paramSensitiveType);
                }
            }
        }
        return obj;
    }

    public static Map<String, Object> getSensitiveArgs(final Field field, final Object object) {
        try {
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
                    argument1 = getSensitiveArgument(field1, argument1);
                    result.put(name1, argument1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
