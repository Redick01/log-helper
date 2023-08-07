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

import com.redick.util.LogUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import static com.redick.constant.TraceTagConstant.NETTY_INVOKE_OVER;
import static com.redick.constant.TraceTagConstant.START_TIME;
import static net.logstash.logback.marker.Markers.append;

/**
 * @author: nff0610
 * @description: Netty inbound interceptor
 * @date: 2023/5/24 15:18
 */
@Slf4j
public class NettyRequestExecDurationInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        String start = MDC.get(START_TIME);
        if (StringUtils.isNotBlank(start)) {
            long startTime = Long.parseLong(start);
            long duration = System.currentTimeMillis() - startTime;
            log.info(LogUtil.customizeMarker(LogUtil.kLOG_KEY_TRACE_TAG, NETTY_INVOKE_OVER)
                            .and(append(LogUtil.kLOG_KEY_DURATION, duration)),
                    NETTY_INVOKE_OVER);
        }
        MDC.clear();
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
