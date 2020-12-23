package com.ruubypay.log.annotation;

import java.lang.annotation.*;

/**
 * 入参字段操作
 * @Author liu_penghui
 * @Date 2019/5/8.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface FieldIgnore {
}
