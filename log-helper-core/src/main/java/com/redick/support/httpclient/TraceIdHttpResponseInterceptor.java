package com.redick.support.httpclient;

import static com.redick.constant.TraceTagConstant.HTTP_CLIENT_EXEC_AFTER;
import com.redick.support.AbstractInterceptor;
import com.redick.util.LogUtil;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

/**
 * @author Redick01
 */
@Slf4j
public class TraceIdHttpResponseInterceptor extends AbstractInterceptor implements HttpResponseInterceptor {

    @Override
    public void process(HttpResponse response, HttpContext context)
            throws HttpException, IOException {
        super.executeAfter(HTTP_CLIENT_EXEC_AFTER);
        log.info(LogUtil.funcEndMarker(response.getEntity()), "HttpClient调用响应数据");
    }
}
