package com.redick.motan.filter;

import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.common.URLParamType;
import com.weibo.api.motan.core.extension.Activation;
import com.weibo.api.motan.core.extension.Scope;
import com.weibo.api.motan.core.extension.Spi;
import com.weibo.api.motan.core.extension.SpiMeta;
import com.weibo.api.motan.filter.Filter;
import com.weibo.api.motan.rpc.Caller;
import com.weibo.api.motan.rpc.Request;
import com.weibo.api.motan.rpc.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Redick01
 */
@Spi(scope = Scope.SINGLETON)
@SpiMeta(name = "log_helper_filter")
@Activation(key = { MotanConstants.NODE_TYPE_SERVICE, MotanConstants.NODE_TYPE_REFERER })
@Slf4j
public class MotanTracerFilter extends AbstractInterceptor implements Filter {

    @Override
    public Response filter(Caller<?> caller, Request request) {
        String nodeName = caller.getUrl().getParameter(URLParamType.nodeType.getName());
        log.info(LogUtil.marker(request.getArguments()), "调用接口[{}]的方法[{}]", request.getInterfaceName(),
                request.getMethodName());
        if (MotanConstants.NODE_TYPE_SERVICE.equals(nodeName)) {
            String traceId = request.getAttachments().get(Tracer.TRACE_ID);
            String spanId = request.getAttachments().get(Tracer.SPAN_ID);
            String parentId = request.getAttachments().get(Tracer.PARENT_ID);
            Tracer.trace(traceId, spanId, parentId);

        } else {
            // get sessionId from MDC
            String traceId = traceId();
            if (StringUtils.isNotBlank(traceId)) {
                request.setAttachment(Tracer.TRACE_ID, traceId);
                request.setAttachment(Tracer.SPAN_ID, spanId());
                request.setAttachment(Tracer.PARENT_ID, parentId());
            }
        }
        return caller.call(request);
    }
}