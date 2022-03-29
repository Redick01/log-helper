package com.redick.reflect;

import com.redick.spi.SPI;

import java.io.UnsupportedEncodingException;

/**
 * @author Redick01
 * @date 2022/3/21 20:49
 */
@SPI("default")
public interface Reflect {

    /**
     * reflect get parameter
     * @param obj parameter
     * @return format parameter
     * @throws UnsupportedEncodingException see {@link UnsupportedEncodingException}
     */
    Object reflect(final Object obj) throws UnsupportedEncodingException;
}
