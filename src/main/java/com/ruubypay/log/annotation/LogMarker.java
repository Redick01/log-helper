package com.ruubypay.log.annotation;

import java.lang.annotation.*;

/**
 * 日志模版注解
 * @Author liu_penghui
 * @Date 2018/10/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface LogMarker {
    /**
     * 接口名
     * @return
     */
    String interfaceName() default "";

    /**
     * 业务描述
     * @return
     */
    String businessDescription() default "";
}
