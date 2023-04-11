package com.redick.support.httpclient5;

import static com.redick.constant.TraceTagConstant.HTTP_CLIENT_EXEC_AFTER;

import com.redick.support.AbstractInterceptor;
import com.redick.util.LogUtil;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpResponseInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;

/**
 * @author Redick01
 */
@Slf4j
public class TraceIdHttpResponse5Interceptor extends AbstractInterceptor implements
        HttpResponseInterceptor {

    @Override
    public void process(HttpResponse response, EntityDetails entity, HttpContext context)
            throws HttpException, IOException {
        super.executeAfter(HTTP_CLIENT_EXEC_AFTER);
        log.info(LogUtil.funcEndMarker(entity.getContentEncoding()), "HttpClient调用响应数据");
    }
}
