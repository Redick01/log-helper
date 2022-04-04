package com.redick.annotation;

import java.lang.annotation.*;

/**
 * @author liu_penghui
 * @date 2018/10/23.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
@SuppressWarnings("all")
public @interface Sensitive {

    /**
     * 数据脱敏起始位置
     * @return
     */
    int start() default Integer.MIN_VALUE;

    /**
     * 数据脱敏终止位置
     * @return
     */
    int end() default Integer.MIN_VALUE;
}
