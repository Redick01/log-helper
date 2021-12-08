package com.ruubypay.log.filter.alibabadubbo;

import com.alibaba.dubbo.common.extension.Activate;
import com.ruubypay.log.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.*;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author liupenghui
 * @date 2020/12/26 11:47 下午
 */
@Activate(group = "provider")
public class DubboxProviderGlobalSessionIdFilter implements com.alibaba.dubbo.rpc.Filter {

    @Trace
    @Override
    public com.alibaba.dubbo.rpc.Result invoke(com.alibaba.dubbo.rpc.Invoker<?> invoker, com.alibaba.dubbo.rpc.Invocation invocation) throws com.alibaba.dubbo.rpc.RpcException {
        // get sessionId from dubbo context attachment
        String sessionId = RpcContext.getContext().getAttachment(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY);
        if (StringUtils.isBlank(sessionId)) {
            if (StringUtils.isNotBlank(TraceContext.traceId())) {
                sessionId = TraceContext.traceId();
            } else {
                sessionId = UUID.randomUUID().toString();
            }
            RpcContext.getContext().setAttachment(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY, sessionId);
        }
        MDC.put(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY, sessionId);
        return invoker.invoke(invocation);
    }
}
