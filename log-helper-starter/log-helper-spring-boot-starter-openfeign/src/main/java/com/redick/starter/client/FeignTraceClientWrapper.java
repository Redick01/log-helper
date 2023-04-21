package com.redick.starter.client;

import static com.redick.constant.TraceTagConstant.OPEN_FEIGN_INVOKE_BEFORE_AFTER;
import static com.redick.constant.TraceTagConstant.OPEN_FEIGN_INVOKE_BEFORE_BEFORE;

import com.google.common.collect.Lists;
import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import feign.Client;
import feign.Request;
import feign.Request.Options;
import feign.Response;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Redick01
 */
public class FeignTraceClientWrapper extends AbstractInterceptor implements Client {

    private final Client delegate;

    public FeignTraceClientWrapper(Client delegate) {
        this.delegate = delegate;
    }

    @Override
    public Response execute(Request request, Options options) throws IOException {
        String traceId = traceId();
        if (StringUtils.isNotBlank(traceId)) {
            request = setRequestHeader(request);
            super.executeBefore(OPEN_FEIGN_INVOKE_BEFORE_BEFORE);
        }
        Response response = delegate.execute(request, options);
        executeAfter(OPEN_FEIGN_INVOKE_BEFORE_AFTER);
        return response;
    }

    private Request setRequestHeader(Request request) {
        Map<String, Collection<String>> headers = request.headers();
        Map<String, Collection<String>> newHeaders = new ConcurrentHashMap<>(16);
        newHeaders.putAll(headers);
        newHeaders.put(Tracer.TRACE_ID, Collections.singleton(traceId()));
        newHeaders.put(Tracer.PARENT_ID, Collections.singleton(parentId()));
        newHeaders.put(Tracer.SPAN_ID, Collections.singleton(spanId()));
        return Request.create(request.httpMethod(), request.url(), newHeaders, request.body(),
                request.charset(), request.requestTemplate());
    }
}
