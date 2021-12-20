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
public class DubboxConsumerGlobalSessionIdFilter implements Filter {

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
