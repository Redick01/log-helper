package com.ruubypay.log.support.spring.handler;

import com.ruubypay.log.support.spring.parser.LogMarkerHandlerParser;
import com.ruubypay.log.support.spring.parser.LogMarkerInterceptorHandlerParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @Author liu_penghui
 * @Date 2018/10/17.
 */
public class LogMarkerNameSpaceHandler extends NamespaceHandlerSupport {
    /**
     * spring配置文件的标签打印通用日志实现类
     */
    private static final String TAG="handler";
    /**
     * 解析interceptor标签
     */
    private static final String INTERCEPTOR="interceptor";

    @Override
    public void init() {
        registerBeanDefinitionParser(TAG, new LogMarkerHandlerParser());
        registerBeanDefinitionParser(INTERCEPTOR, new LogMarkerInterceptorHandlerParser());
    }
}
