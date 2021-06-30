package com.ruubypay.log;

import org.slf4j.TtlMDCAdapter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
/**
 * @author liupenghui
 * @date 2021/6/30 3:37 下午
 */
public class TtlMDCAdapterInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        // 加载自定义的MDCAdapter
        TtlMDCAdapter.getInstance();
    }
}
