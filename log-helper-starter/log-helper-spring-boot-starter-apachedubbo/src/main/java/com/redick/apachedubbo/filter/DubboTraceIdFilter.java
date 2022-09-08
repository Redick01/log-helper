package com.redick.apachedubbo.filter;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @author liupenghui
 *  2020/12/26 11:47 下午
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER_SIDE, CommonConstants.CONSUMER_SIDE})
public class DubboTraceIdFilter extends AbstractInterceptor implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String side = invoker.getUrl().getParameter(CommonConstants.SIDE_KEY);
        try {
            if (CommonConstants.CONSUMER_SIDE.equals(side)) {
                log.info(LogUtil.marker(invocation.getArguments()), "调用接口[{}]的方法[{}]", invoker.getInterface().getSimpleName(),
                        invocation.getMethodName());
                // get sessionId from MDC
                String traceId = traceId();
                if (StringUtils.isNotBlank(traceId)) {
                    RpcContext.getServiceContext().setAttachment(Tracer.TRACE_ID, traceId);
                    RpcContext.getServiceContext().setAttachment(Tracer.SPAN_ID, spanId());
                    RpcContext.getServiceContext().setAttachment(Tracer.PARENT_ID, parentId());
                }
            } else {
                String traceId = RpcContext.getServiceContext().getAttachment(Tracer.TRACE_ID);
                String spanId = RpcContext.getServiceContext().getAttachment(Tracer.SPAN_ID);
                String parentId = RpcContext.getServiceContext().getAttachment(Tracer.PARENT_ID);
                Tracer.trace(traceId, spanId, parentId);
                log.info(LogUtil.marker(invocation.getArguments()), "调用接口[{}]的方法[{}]", invoker.getInterface().getSimpleName(),
                        invocation.getMethodName());
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
