package com.redick.spi;

import java.lang.annotation.*;

/**
 * @author liupenghui
 * @date 2021/12/24 3:18 下午
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {

    /**
     * value
     * @return value
     */
    String value() default "";
}
