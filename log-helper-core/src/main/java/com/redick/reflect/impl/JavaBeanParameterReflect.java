package com.redick.reflect.impl;

import com.redick.annotation.FieldIgnore;
import com.redick.annotation.ValidChild;
import com.redick.reflect.AbstractReflect;
import com.redick.spi.Join;
import com.redick.util.SensitiveFieldUtil;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author Redick01
 * @date 2022/3/22 19:44
 */
@Join
public class JavaBeanParameterReflect extends AbstractReflect {

    @Override
    public Object doReflect(Object obj) {
        Field[] fields = FieldUtils.getAllFields(obj.getClass());
        HashMap<String, Object> result = new HashMap<>(fields.length);
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object argument = field.get(obj);
                String name = field.getName();
                if (null != field.getAnnotation(ValidChild.class)) {
                    argument = doReflect(argument);
                }
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
        return result;
    }
}
