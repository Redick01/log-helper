package com.redick.annotation;

import java.lang.annotation.*;

/**
 * 日志模版注解
 * @author liu_penghui
 *  2018/10/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface LogMarker {

    /**
     * @return interface name
     */
    String interfaceName() default "";

    /**
     * @return business description
     */
    String businessDescription() default "";
}
