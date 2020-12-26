package com.ruubypay.log.filter;

import com.ruubypay.log.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

/**
 * @author liupenghui
 * @date 2020/12/26 11:47 下午
 */
public class DubboProviderGlobalSessionIdFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // get sessionId from MDC
        String sessionId = MDC.get(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY);
        if (StringUtils.isNotBlank(sessionId)) {
            RpcContext.getContext().setAttachment(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY, sessionId);
        }
        return invoker.invoke(invocation);
    }
}
