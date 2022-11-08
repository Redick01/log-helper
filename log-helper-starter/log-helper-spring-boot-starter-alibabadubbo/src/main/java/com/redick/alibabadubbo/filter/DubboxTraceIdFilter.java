package com.redick.alibabadubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.skywalking.apm.toolkit.trace.Trace;

/**
 * @author liupenghui
 *  2020/12/26 11:47 下午
 */
@Activate(group = {Constants.CONSUMER_SIDE, Constants.PROVIDER_SIDE})
@Slf4j
public class DubboxTraceIdFilter extends AbstractInterceptor implements Filter {

    @Trace
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // get traceId from dubbo context attachment
        try {
            String side = invoker.getUrl().getParameter(Constants.SIDE_KEY);
            log.info(LogUtil.marker(invocation.getArguments()), "调用接口[{}]的方法[{}]", invoker.getInterface().getSimpleName(),
                    invocation.getMethodName());
            if (Constants.PROVIDER_SIDE.equals(side)) {
                String traceId = RpcContext.getContext().getAttachment(LogUtil.kLOG_KEY_TRACE_ID);
                String spanId = RpcContext.getContext().getAttachment(Tracer.SPAN_ID);
                String parentId = RpcContext.getContext().getAttachment(Tracer.PARENT_ID);
                Tracer.trace(traceId, spanId, parentId);
            } else {
                // get traceId from MDC
                String traceId = traceId();
                if (StringUtils.isNotBlank(traceId)) {
                    RpcContext.getContext().setAttachment(Tracer.TRACE_ID, traceId);
                    RpcContext.getContext().setAttachment(Tracer.SPAN_ID, spanId());
                    RpcContext.getContext().setAttachment(Tracer.PARENT_ID, parentId());
                }
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
