package com.redick.spi.test;

import com.redick.spi.ExtensionFactory;
import com.redick.spi.ExtensionLoader;
import com.redick.spi.SpiExtensionFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author liupenghui
 * @date 2022/1/4 6:06 下午
 */
public class SpiExtensionFactoryTest {

    @Test
    public void getExtensionTest() {
        ExtensionFactory extensionFactory = new SpiExtensionFactory();
        TestSPI testSPI = ExtensionLoader.getExtensionLoader(TestSPI.class).getJoin("testSPI1");
        testSPI.test();
    }
}
