package com.ruubypay.log.filter;

import com.ruubypay.log.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author liupenghui
 * @date 2020/12/26 11:47 下午
 */
public class DubboConsumerGlobalSessionIdFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // get sessionId from dubbo context attachment
        try {
            String sessionId = RpcContext.getContext().getAttachment(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY);
            if (StringUtils.isBlank(sessionId)) {
                sessionId = UUID.randomUUID().toString();
                RpcContext.getContext().setAttachment(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY, sessionId);
            }
            MDC.put(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY, sessionId);
            return invoker.invoke(invocation);
        } finally {
            MDC.remove(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY);
        }

    }
}
