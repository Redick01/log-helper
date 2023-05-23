/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
