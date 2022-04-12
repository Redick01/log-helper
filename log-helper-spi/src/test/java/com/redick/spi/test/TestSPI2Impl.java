package com.redick.spi.test;

import com.redick.spi.Join;

/**
 * @author liupenghui
 *  2022/1/10 2:56 下午
 */
@Join
public class TestSPI2Impl implements TestSPI {

    @Override
    public void test() {
        System.out.println("test2");
    }
}
