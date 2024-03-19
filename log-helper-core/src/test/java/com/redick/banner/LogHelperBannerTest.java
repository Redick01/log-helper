package com.redick.banner;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

/**
 * @author: Redick01
 * @date: 2024/1/26 18:15
 */
public class LogHelperBannerTest {
    @Mock
    Logger log;
    @InjectMocks
    LogHelperBanner logHelperBanner;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAfterPropertiesSet() throws Exception {
        logHelperBanner.afterPropertiesSet();
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme