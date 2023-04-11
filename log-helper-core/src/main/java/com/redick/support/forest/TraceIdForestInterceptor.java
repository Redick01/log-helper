package com.redick.support.forest;

import static com.redick.constant.TraceTagConstant.FOREST_EXEC_AFTER;
import static com.redick.constant.TraceTagConstant.FOREST_EXEC_BEFORE;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.redick.constant.TraceIdDefine;
import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Redick01
 */
@Slf4j
public class TraceIdForestInterceptor extends AbstractInterceptor implements Interceptor<Object> {

    @Override
    public boolean beforeExecute(ForestRequest request) {
        log.info(LogUtil.funcStartMarker(request.getBody()), "开始forest请求处理");
        if (StringUtils.isNotBlank(traceId())) {
            request.addHeader(TraceIdDefine.TRACE_ID, traceId());
            request.addHeader(Tracer.SPAN_ID, spanId());
            request.addHeader(Tracer.PARENT_ID, parentId());
            super.executeBefore(FOREST_EXEC_BEFORE);
        } else {
            log.info(LogUtil.marker(), "current thread have not the traceId!");
        }
        return true;
    }

    @Override
    public void afterExecute(ForestRequest request, ForestResponse response) {
        super.executeAfter(FOREST_EXEC_AFTER);
        log.info(LogUtil.funcEndMarker(response.getContent()), "forest请求处理完毕");
    }
}
