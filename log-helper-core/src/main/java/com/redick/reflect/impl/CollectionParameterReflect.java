package com.redick.reflect.impl;

import com.redick.SensitiveDataConverter;
import com.redick.reflect.AbstractReflect;

/**
 * @author Redick01
 * @date 2022/3/22 19:43
 */
public class CollectionParameterReflect extends AbstractReflect {

    @Override
    public Object doReflect(Object obj) {
        // 脱敏后的参数
        try {
            return SensitiveDataConverter.invokeMsg(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "parameter is null!";
    }
}
