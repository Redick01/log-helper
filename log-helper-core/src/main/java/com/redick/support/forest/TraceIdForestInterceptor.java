package com.redick.support.forest;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.redick.common.TraceIdDefine;
import com.redick.support.AbstractInterceptor;
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
        } else {
            log.info(LogUtil.marker(), "current thread have not the traceId!");
        }
        return true;
    }

    @Override
    public void afterExecute(ForestRequest request, ForestResponse response) {
        log.info(LogUtil.marker(response.getContent()), "forest请求处理完毕");
    }
}
