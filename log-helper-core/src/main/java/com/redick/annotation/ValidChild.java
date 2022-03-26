package com.redick.annotation;

import java.lang.annotation.*;

/**
 * @author liupenghui
 * @date 2021/12/18 11:07 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface ValidChild {
}
