package com.ruubypay.log.filter.web;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.*;

import java.lang.reflect.Method;

/**
 * @author liupenghui
 * @date 2021/6/30 11:41 上午
 */
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration;
        interceptorRegistration = registry.addInterceptor(new WebHandlerMethodInterceptor());
        //这里是为了兼容springboot 1.5.X，1.5.x没有order这个方法
        try{
            Method method = ReflectUtil.getMethod(InterceptorRegistration.class, "order", Integer.class);
            if (ObjectUtil.isNotNull(method)){
                method.invoke(interceptorRegistration, 1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
