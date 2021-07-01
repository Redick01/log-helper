package com.ruubypay.log.filter.alibabadubbo;

import com.alibaba.dubbo.rpc.Filter;
import com.ruubypay.log.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

/**
 * @author liupenghui
 * @date 2021/6/24 11:08 下午
 */
public class DubboxConsumerGlobalSessionIdFilter implements Filter {

    @Override
    public com.alibaba.dubbo.rpc.Result invoke(com.alibaba.dubbo.rpc.Invoker<?> invoker, com.alibaba.dubbo.rpc.Invocation invocation) throws com.alibaba.dubbo.rpc.RpcException {
        // get sessionId from MDC
        String sessionId = MDC.get(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY);
        if (StringUtils.isNotBlank(sessionId)) {
            RpcContext.getContext().setAttachment(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY, sessionId);
        }
        return invoker.invoke(invocation);
    }
}
