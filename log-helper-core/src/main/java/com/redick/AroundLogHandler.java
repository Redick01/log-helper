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

package com.redick;

import com.redick.annotation.LogMarker;
import com.redick.proxy.AroundLogProxyChain;
import com.redick.reflect.ReflectHandler;
import com.redick.support.AbstractInterceptor;
import com.redick.tracer.Tracer;
import com.redick.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.reflect.Method;

/**
 * @author Redick01
 */
public class AroundLogHandler extends AbstractInterceptor {

    /**
     * 环绕处理
     *
     * @param chain 代理对象
     * @return 执行结果
     * @throws Throwable throwable
     */
    public Object around(final AroundLogProxyChain chain) throws Throwable {
        Logger logger = getRealLogger(chain);
        mdcLogMarkerParam(chain);
        Tracer.trace(traceId(), spanId(), parentId());
        logger.info(LogUtil.processBeginMarker(ReflectHandler.getInstance().getRequestParameter(chain)),
                LogUtil.kTYPE_BEGIN);
        long start = System.currentTimeMillis();
        Object o = null;
        try {
            o = chain.getProceed();
        } finally {
            logger.info(LogUtil.processSuccessDoneMarker(o != null
                            ? ReflectHandler.getInstance().getResponseParameter(o) : null,
                    System.currentTimeMillis() - start), LogUtil.kTYPE_DONE);
            Tracer.remove();
        }
        return o;
    }

    /**
     * 处理LogMaker注解中的参数
     *
     * @param chain chain
     */
    private void mdcLogMarkerParam(AroundLogProxyChain chain) {
        Method method = chain.getMethod();
        if (null != method.getAnnotation(LogMarker.class)) {
            if (StringUtils.isNotBlank(method.getAnnotation(LogMarker.class).businessDescription())) {
                MDC.put(LogUtil.kLOG_KEY_REQUEST_TYPE, method.getAnnotation(LogMarker.class).businessDescription());
            }
            if (StringUtils.isNotBlank(method.getAnnotation(LogMarker.class).interfaceName())) {
                MDC.put(LogUtil.kLOG_KEY_INTERFACE_NAME, method.getAnnotation(LogMarker.class).interfaceName());
            }
        }
    }

    /**
     * 获取实际业务逻辑实现类的logger对象
     *
     * @param aroundLogProxyChain 切点
     * @return 返回能够真正打印日志位置的包名Logger
     */
    private Logger getRealLogger(final AroundLogProxyChain aroundLogProxyChain) {
        return LoggerFactory.getLogger(aroundLogProxyChain.getClazz().getCanonicalName());
    }
}
