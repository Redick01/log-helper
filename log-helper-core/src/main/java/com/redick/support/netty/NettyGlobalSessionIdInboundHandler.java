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

package com.redick.support.netty;

import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import static com.redick.constant.TraceTagConstant.NETTY_INVOKE_START;
import static com.redick.constant.TraceTagConstant.START_TIME;

/**
 * @author: nff0610
 * @description: Netty inbound interceptor
 * @date: 2023/5/24 15:06
 */
@Slf4j
public class NettyGlobalSessionIdInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 只处理Http请求
        if (msg instanceof HttpRequest && msg instanceof HttpContent) {
            HttpRequest request = (HttpRequest) msg;
            // 从request header获取链路参数
            HttpHeaders headers = request.headers();
            String traceId = headers.get(Tracer.TRACE_ID);
            Tracer.trace(traceId, headers.get(Tracer.SPAN_ID), headers.get(Tracer.PARENT_ID));
            // 记录请求开始时间
            MDC.put(START_TIME, String.valueOf(System.currentTimeMillis()));
            log.info(LogUtil.customizeMarker(LogUtil.kLOG_KEY_TRACE_TAG, NETTY_INVOKE_START), NETTY_INVOKE_START);
        }
        ctx.fireChannelRead(msg);
    }
}
