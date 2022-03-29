package com.redick.loghelper.listener;

import org.slf4j.TtlMDCAdapter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author Redick
 * @date 2022/3/29 10:17 下午
 */
@SuppressWarnings("all")
@Component
public class ApplicationStartedListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            TtlMDCAdapter.getInstance();
        }
    }
}
