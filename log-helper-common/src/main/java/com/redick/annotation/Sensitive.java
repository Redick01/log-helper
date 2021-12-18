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
public @interface Sensitive {
    /**
     * 参数脱敏类型
     * @return 参数脱敏类型
     */
    String paramSensitiveType() default "";

    /**
     * 是否脱敏
     * @return 是否脱敏
     */
    boolean isSensitive() default false;
}
