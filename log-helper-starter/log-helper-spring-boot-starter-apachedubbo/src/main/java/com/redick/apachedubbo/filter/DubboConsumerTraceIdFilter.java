package com.redick.apachedubbo.filter;

import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

/**
 * @author liupenghui
 *  2020/12/26 11:47 下午
 */
@Slf4j
@Activate(group = "consumer")
public class DubboConsumerTraceIdFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            log.info(LogUtil.marker(invocation.getArguments()), "开始调用接口[{}]的方法[{}]", invoker.getInterface().getSimpleName(),
                    invocation.getMethodName());
            // get sessionId from MDC
            String traceId = MDC.get(LogUtil.kLOG_KEY_TRACE_ID);
            if (StringUtils.isNotBlank(traceId)) {
                RpcContext.getServiceContext().setAttachment(LogUtil.kLOG_KEY_TRACE_ID, traceId);
            }
            result = invoker.invoke(invocation);
        } finally {
            stopWatch.stop();
            log.info(LogUtil.marker(), "结束接口[{}]中方法[{}]的调用,耗时为:[{}]毫秒", invoker.getInterface().getSimpleName(),
                    invocation.getMethodName(),
                    stopWatch.getTime());
        }

        return result;
    }
}
