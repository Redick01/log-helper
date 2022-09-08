package com.redick.tracer;

import com.redick.tracer.Tracer.TracerBuilder;
import org.junit.Test;

/**
 * @author Redick01
 */
public class TracerTest {

    @Test
    public void childTest() {
        Tracer tracer = new TracerBuilder()
                .traceId("1112321")
                .build();
        tracer.buildSpan();

        Tracer tracer1 = new TracerBuilder()
                .parentId(tracer.getParentId())
                .traceId(tracer.getTraceId())
                .spanId(tracer.getSpanId())
                .build();
        tracer1.buildSpan();
        Tracer tracer2 = new TracerBuilder()
                .parentId(tracer1.getParentId())
                .traceId(tracer1.getTraceId())
                .spanId(tracer1.getSpanId())
                .build();
        tracer2.buildSpan();
        System.out.println(tracer);
        System.out.println(tracer1);
        System.out.println(tracer2);
    }
}
