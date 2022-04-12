package com.redick.spi.test;

import com.redick.spi.ExtensionFactory;
import com.redick.spi.ExtensionLoader;
import com.redick.spi.SpiExtensionFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author liupenghui
 */
public class SpiExtensionFactoryTest {

    @Test
    public void getExtensionTest() {
        TestSPI testSPI = ExtensionLoader.getExtensionLoader(TestSPI.class).getJoin("testSPI2");
        testSPI.test();
    }
}
