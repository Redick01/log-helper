package com.redick.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
