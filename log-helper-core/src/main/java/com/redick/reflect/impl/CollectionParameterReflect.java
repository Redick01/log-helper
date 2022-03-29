package com.redick.reflect.impl;

import com.redick.SensitiveDataConverter;
import com.redick.reflect.Reflect;
import com.redick.spi.Join;

import java.io.UnsupportedEncodingException;

/**
 * @author Redick01
 * @date 2022/3/22 19:43
 */
@Join
public class CollectionParameterReflect implements Reflect {

    @Override
    public Object reflect(Object obj) throws UnsupportedEncodingException {
        // 脱敏后的参数
        try {
            return SensitiveDataConverter.invokeMsg(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "parameter is null!";
    }
}
