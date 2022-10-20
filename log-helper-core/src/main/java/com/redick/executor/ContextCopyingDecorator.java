package com.redick.executor;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author Redick01
 */
public class ContextCopyingDecorator implements TaskDecorator {

    @NotNull
    @Override
    public Runnable decorate(Runnable runnable) {
        RequestAttributes context = RequestContextHolder.currentRequestAttributes();
        Map<String,String> previous = MDC.getCopyOfContextMap();
        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(context);
                MDC.setContextMap(previous);
                runnable.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
                MDC.clear();
            }
        };
    }
}
