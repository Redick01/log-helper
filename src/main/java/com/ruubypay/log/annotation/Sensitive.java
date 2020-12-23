package com.ruubypay.log.annotation;

import java.lang.annotation.*;

/**
 * @Author liu_penghui
 * @Date 2018/10/23.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface Sensitive {
    /**
     * 参数脱敏类型
     * @return
     */
    String paramSensitiveType() default "";

    /**
     * 是否脱敏
     * @return
     */
    boolean isSensitive() default false;
}
