package com.redick.apachedubbo.filter;

import com.redick.common.TraceIdDefine;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author liupenghui
 *  2020/12/26 11:47 下午
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER_SIDE, CommonConstants.CONSUMER_SIDE})
public class DubboTraceIdFilter implements Filter {

    @Override
    @Trace
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String side = invoker.getUrl().getParameter(CommonConstants.SIDE_KEY);
        try {
            if (CommonConstants.CONSUMER_SIDE.equals(side)) {
                log.info(LogUtil.marker(invocation.getArguments()), "调用接口[{}]的方法[{}]", invoker.getInterface().getSimpleName(),
                        invocation.getMethodName());
                // get sessionId from MDC
                String traceId = MDC.get(LogUtil.kLOG_KEY_TRACE_ID);
                if (StringUtils.isNotBlank(traceId)) {
                    RpcContext.getServiceContext().setAttachment(LogUtil.kLOG_KEY_TRACE_ID, traceId);
                }
                return invoker.invoke(invocation);
            } else {
                String traceId = RpcContext.getServiceContext().getAttachment(LogUtil.kLOG_KEY_TRACE_ID);
                if (StringUtils.isBlank(traceId)) {
                    if (StringUtils.isNotBlank(TraceContext.traceId()) && !TraceIdDefine.SKYWALKING_NO_ID.equals(TraceContext.traceId())) {
                        traceId = TraceContext.traceId();
                    } else {
                        traceId = UUID.randomUUID().toString();
                    }
                    RpcContext.getServiceContext().setAttachment(LogUtil.kLOG_KEY_TRACE_ID, traceId);
                }
                MDC.put(LogUtil.kLOG_KEY_TRACE_ID, traceId);
                log.info(LogUtil.marker(invocation.getArguments()), "调用接口[{}]的方法[{}]", invoker.getInterface().getSimpleName(),
                        invocation.getMethodName());
                return invoker.invoke(invocation);
            }
        } finally {
            stopWatch.stop();
            log.info(LogUtil.marker(), "结束接口[{}]中方法[{}]的调用,耗时为:[{}]毫秒", invoker.getInterface().getSimpleName(),
                    invocation.getMethodName(),
                    stopWatch.getTime());
        }
    }
}
