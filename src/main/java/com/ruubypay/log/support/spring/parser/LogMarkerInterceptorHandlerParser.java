package com.ruubypay.log.support.spring.parser;

import com.ruubypay.log.aop.AopInterceptor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * @Author liu_penghui
 * @Date 2018/10/17.
 */
public class LogMarkerInterceptorHandlerParser extends AbstractSingleBeanDefinitionParser {

    /**
     * handler引用
     */
    private static final String HANDLER_REF="handler";

    @Override
    protected Class<?> getBeanClass(final Element element) {
        return AopInterceptor.class;
    }

    @Override
    protected void doParse(final Element element, final BeanDefinitionBuilder builder) {
        String handlerBean = element.getAttribute(HANDLER_REF);
        builder.addConstructorArgReference(handlerBean);
    }
}
