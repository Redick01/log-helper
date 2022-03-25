package com.redick.alibabadubbo.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.redick.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author liupenghui
 * @date 2020/12/26 11:47 下午
 */
@Activate(group = "provider")
public class DubboxProviderTraceIdFilter implements Filter {

    @Trace
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // get traceId from dubbo context attachment
        String traceId = RpcContext.getContext().getAttachment(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY);
        if (StringUtils.isBlank(traceId)) {
            if (StringUtils.isNotBlank(TraceContext.traceId())) {
                traceId = TraceContext.traceId();
            } else {
                traceId = UUID.randomUUID().toString();
            }
            RpcContext.getContext().setAttachment(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY, traceId);
        }
        MDC.put(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY, traceId);
        return invoker.invoke(invocation);
    }
}
