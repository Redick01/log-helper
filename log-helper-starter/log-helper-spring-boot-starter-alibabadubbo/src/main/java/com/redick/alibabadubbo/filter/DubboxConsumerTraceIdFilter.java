package com.redick.alibabadubbo.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.redick.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * @author liupenghui
 * @date 2021/6/24 11:08 下午
 */
@Activate(group = "consumer")
public class DubboxConsumerTraceIdFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // get traceId from MDC
        String traceId = MDC.get(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY);
        if (StringUtils.isNotBlank(traceId)) {
            RpcContext.getContext().setAttachment(LogUtil.kLOG_KEY_GLOBAL_SESSION_ID_KEY, traceId);
        }
        return invoker.invoke(invocation);
    }

}
