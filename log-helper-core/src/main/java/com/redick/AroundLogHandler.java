package com.redick;

import com.redick.annotation.LogMarker;
import com.redick.proxy.AroundLogProxyChain;
import com.redick.reflect.ReflectHandler;
import com.redick.tracer.Tracer;
import com.redick.tracer.Tracer.TracerBuilder;
import com.redick.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.reflect.Method;

/**
 * @author Redick01
 */
public class AroundLogHandler {

    /**
     * 环绕处理
     * @param chain 代理对象
     * @return 执行结果
     */
    public Object around(final AroundLogProxyChain chain) {
        Logger logger = getRealLogger(chain);
        mdcLogMarkerParam(chain);
        Tracer tracer = Tracer.traceThreadLocal.get();
        if (null == tracer) {
            String traceId = MDC.get(Tracer.TRACE_ID);
            String spanId = MDC.get(Tracer.SPAN_ID);
            String parentId = MDC.get(Tracer.PARENT_ID);
            tracer = new TracerBuilder()
                    .traceId(traceId)
                    .spanId(null == spanId ? null : Integer.parseInt(spanId))
                    .parentId(null == parentId ? null : Integer.parseInt(parentId))
                    .build();
            tracer.buildSpan();
        }
        logger.info(LogUtil.processBeginMarker(ReflectHandler.getInstance().getRequestParameter(chain)),
                LogUtil.kTYPE_BEGIN);
        long start = System.currentTimeMillis();
        Object o = null;
        try {
            o = chain.getProceed();
        } catch (Throwable throwable) {
            logger.error(LogUtil.exceptionMarker(), LogUtil.kTYPE_EXCEPTION, throwable);
        } finally {
            logger.info(LogUtil.processSuccessDoneMarker(o != null ? ReflectHandler.getInstance().getResponseParameter(o) : null,
                    System.currentTimeMillis() - start), LogUtil.kTYPE_DONE);
            MDC.clear();
        }
        return o;
    }

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
     * @param aroundLogProxyChain 切点
     * @return 返回能够真正打印日志位置的包名Logger
     */
    private Logger getRealLogger(final AroundLogProxyChain aroundLogProxyChain) {
        return LoggerFactory.getLogger(aroundLogProxyChain.getClazz().getCanonicalName());
    }
}
