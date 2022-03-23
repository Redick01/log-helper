package com.redick.reflect;


/**
 * @author Redick01
 * @date 2022/3/22 13:59
 */
public abstract class AbstractReflect implements Reflect {

    @Override
    public Object reflect(Object obj) {
        return doReflect(obj);
    }

    /**
     * 参数操作
     * @param obj parameter obj
     * @return after process obj
     */
    public abstract Object doReflect(Object obj);
}
