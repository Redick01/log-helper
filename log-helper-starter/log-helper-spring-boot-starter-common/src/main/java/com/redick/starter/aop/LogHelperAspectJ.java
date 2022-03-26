package com.redick.starter.aop;

import com.redick.AroundLogHandler;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Redick01
 * @date 2022/3/25 14:36
 */
@Aspect
public class LogHelperAspectJ {

    private final AroundLogHandler aroundLogHandler = new AroundLogHandler();
}
