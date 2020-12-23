package com.ruubypay.log.util;

import com.ruubypay.log.aop.proxy.AroundLogProxyChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取实际打印日志包路径
 * @Author liu_penghui
 * @Date 2018/10/19.
 */
public class RealLoggerPathUtil {

    /**
     * 获取实际业务逻辑实现类的logger对象
     * @param aroundLogProxyChain 切点
     * @return
     */
    public static Logger getRealLogger(AroundLogProxyChain aroundLogProxyChain) {
        return LoggerFactory.getLogger(aroundLogProxyChain.getClazz().getCanonicalName());
    }
}
