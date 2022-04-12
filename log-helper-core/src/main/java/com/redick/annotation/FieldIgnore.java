package com.redick.annotation;

import java.lang.annotation.*;

/**
 * 入参字段操作
 * @author liu_penghui
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface FieldIgnore {
}
