package com.redick.starter.annotation;

import com.redick.starter.importer.LogHelperImportSelector;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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
@Import({LogHelperImportSelector.class})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public @interface LogHelperEnable {
}
