package com.redick.boot.annotation;

import com.redick.boot.ImportedClassSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author liupenghui
 * @date 2021/12/18 10:58 下午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ImportedClassSelector.class})
public @interface LogHelperEnable {
}
