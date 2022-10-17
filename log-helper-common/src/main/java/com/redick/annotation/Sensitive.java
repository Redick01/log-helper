package com.redick.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liu_penghui
 *  2018/10/23.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
@SuppressWarnings("all")
public @interface Sensitive {

    /**
     * 数据脱敏起始位置
     * @return 脱敏起始位置
     */
    int start() default Integer.MIN_VALUE;

    /**
     * 数据脱敏终止位置
     * @return 脱敏结束为止
     */
    int end() default Integer.MIN_VALUE;
}
