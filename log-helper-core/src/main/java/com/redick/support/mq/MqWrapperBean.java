package com.redick.support.mq;

import com.redick.common.TraceIdDefine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * MQ消息Bean的包装Bean
 * @author liupenghui
 * @date 2021/12/10 1:22 下午
 */
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class MqWrapperBean<T> {

    private String traceId;

    /**
     * MQ业务数据
     */
    private T t;

    public MqWrapperBean(T t) {
        this.t = t;
        String newTraceId = MDC.get(TraceIdDefine.TRACE_ID);
        if (StringUtils.isNotBlank(newTraceId)) {
            this.traceId = newTraceId;
        }
    }
}
