package com.redick.spi.test;

import com.redick.spi.Join;

/**
 * @author liupenghui
 * @date 2022/1/4 6:06 下午
 */
@Join
public class TestSPI1Impl implements TestSPI {

    @Override
    public void test() {
        System.out.println("test1");
    }
}
