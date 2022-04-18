package com.redick.alibabadubbo.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.MDC;

/**
 * @author liupenghui
 *  2021/6/24 11:08 下午
 */
@Activate(group = "consumer")
@Slf4j
public class DubboxConsumerTraceIdFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            log.info(LogUtil.marker(invocation.getArguments()), "开始调用接口[{}]的方法[{}]", invoker.getInterface().getSimpleName(),
                    invocation.getMethodName());
            // get traceId from MDC
            String traceId = MDC.get(LogUtil.kLOG_KEY_TRACE_ID);
            if (StringUtils.isNotBlank(traceId)) {
                RpcContext.getContext().setAttachment(LogUtil.kLOG_KEY_TRACE_ID, traceId);
            }
            return invoker.invoke(invocation);
        } finally {
            stopWatch.stop();
            log.info(LogUtil.marker(), "结束接口[{}]中方法[{}]的调用,耗时为:[{}]毫秒", invoker.getInterface().getSimpleName(),
                    invocation.getMethodName(),
                    stopWatch.getTime());
        }
    }

}
