package com.redick.reflect.impl;

import com.google.common.collect.Maps;
import com.redick.reflect.Reflect;
import com.redick.spi.Join;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author Redick01
 * @date 2022/3/22 13:55
 */
@Slf4j
@Join
public class HttpServletRequestReflect implements Reflect {

    @Override
    public Object reflect(Object obj) throws UnsupportedEncodingException {
        Map<String, String[]> result = Maps.newConcurrentMap();
        if (obj instanceof HttpServletRequest) {
            try {
                ((HttpServletRequest) obj).setCharacterEncoding("UTF-8");
                Enumeration<String> names = ((HttpServletRequest) obj).getParameterNames();
                while (names.hasMoreElements()) {
                    String name = names.nextElement();
                    String [] value = ((HttpServletRequest) obj).getParameterValues(name);
                    result.put(name, value);
                }
            } catch (UnsupportedEncodingException e) {
                log.error("UnsupportedEncodingException", e);
            }
        }
        return result;
    }
}
