package com.redick.support.spring.parser;

import com.redick.AroundLogHandler;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * @author Redick01
 *  2018/10/17.
 */
@SuppressWarnings("all")
public class LogMarkerHandlerParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected Class<?> getBeanClass(Element element) {
        return AroundLogHandler.class;
    }
}
