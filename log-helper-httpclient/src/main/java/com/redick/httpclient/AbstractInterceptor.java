package com.redick.httpclient;

import com.redick.common.GlobalSessionIdDefine;
import org.slf4j.MDC;

/**
 * @author Redick01
 * @date 2022/3/24 21:01
 */
public abstract class AbstractInterceptor {

    public String traceId() {
        return MDC.get(GlobalSessionIdDefine.GLOBAL_SESSION_ID_KEY);
    }
}
