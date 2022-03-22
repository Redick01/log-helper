package com.redick.reflect;

import com.redick.aop.proxy.AroundLogProxyChain;

import java.io.UnsupportedEncodingException;

/**
 * @author Redick01
 * @date 2022/3/21 20:49
 */
public interface Reflect {

    /**
     * reflect get parameter
     * @param obj parameter
     * @return format parameter
     */
    Object reflect(final AroundLogProxyChain obj) throws UnsupportedEncodingException;
}
