package com.redick.support.spring.handler;


import com.redick.support.spring.parser.LogMarkerHandlerParser;
import com.redick.support.spring.parser.LogMarkerInterceptorHandlerParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author Redick01
 * @date 2018/10/17.
 */
public class LogMarkerNameSpaceHandler extends NamespaceHandlerSupport {
    /**
     * spring配置文件的标签打印通用日志实现类
     */
    private static final String TAG = "handler";
    /**
     * 解析interceptor标签
     */
    private static final String INTERCEPTOR = "interceptor";

    @Override
    public void init() {
        registerBeanDefinitionParser(TAG, new LogMarkerHandlerParser());
        registerBeanDefinitionParser(INTERCEPTOR, new LogMarkerInterceptorHandlerParser());
    }
}
