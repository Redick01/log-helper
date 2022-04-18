package com.redick.alibabadubbo.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author liupenghui
 *  2020/12/26 11:47 下午
 */
@Activate(group = "provider")
@Slf4j
public class DubboxProviderTraceIdFilter implements Filter {

    @Trace
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // get traceId from dubbo context attachment
        try {
            String traceId = RpcContext.getContext().getAttachment(LogUtil.kLOG_KEY_TRACE_ID);
            if (StringUtils.isBlank(traceId)) {
                if (StringUtils.isNotBlank(TraceContext.traceId())) {
                    traceId = TraceContext.traceId();
                } else {
                    traceId = UUID.randomUUID().toString();
                }
                RpcContext.getContext().setAttachment(LogUtil.kLOG_KEY_TRACE_ID, traceId);
            }
            MDC.put(LogUtil.kLOG_KEY_TRACE_ID, traceId);
            log.info(LogUtil.marker(invocation.getArguments()), "开始调用接口[{}]的方法[{}]", invoker.getInterface().getSimpleName(),
                    invocation.getMethodName());
            return invoker.invoke(invocation);
        } finally {
            stopWatch.stop();
            log.info(LogUtil.marker(), "结束接口[{}]中方法[{}]的调用,耗时为:[{}]毫秒", invoker.getInterface().getSimpleName(),
                    invocation.getMethodName(),
                    stopWatch.getTime());
        }
    }
}
