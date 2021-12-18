package com.redick.annotation;

import java.lang.annotation.*;

/**
 * 入参字段操作
 * @author liu_penghui
 * @date 2019/5/8.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface FieldIgnore {
}
