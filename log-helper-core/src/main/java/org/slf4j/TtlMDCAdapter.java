package org.slf4j;


import com.alibaba.ttl.TransmittableThreadLocal;
import org.slf4j.spi.MDCAdapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 重写logback的LogbackMDCAdapter，注意MDC是MDCAdapter是包级私有，所以重写的报名应该是org.slf4j
 * 用TransmittableThreadLocal替换ThreadLocal，解决多线程，异步情况x-glabal-sessionId无法传递问题
 * @author liupenghui
 * @date 2021/6/30 2:38 下午
 */
public class TtlMDCAdapter implements MDCAdapter {

    private final ThreadLocal<Map<String, String>> copyOnInheritThreadLocal = new TransmittableThreadLocal<>();

    private static final int WRITE_OPERATION = 1;
    private static final int MAP_COPY_OPERATION = 2;

    static TtlMDCAdapter mtcMDCAdapter;

    static {
        mtcMDCAdapter = new TtlMDCAdapter();
        MDC.mdcAdapter = mtcMDCAdapter;
    }

    public static MDCAdapter getInstance() {
        return mtcMDCAdapter;
    }

    final ThreadLocal<Integer> lastOperation = new ThreadLocal<Integer>();

    private Integer getAndSetLastOperation(int op) {
        Integer lastOp = lastOperation.get();
        lastOperation.set(op);
        return lastOp;
    }

    private boolean wasLastOpReadOrNull(Integer lastOp) {
        return lastOp == null || lastOp.intValue() == MAP_COPY_OPERATION;
    }

    private Map<String, String> duplicateAndInsertNewMap(Map<String, String> oldMap) {
        Map<String, String> newMap = Collections.synchronizedMap(new HashMap<String, String>());
        if (oldMap != null) {
            // we don't want the parent thread modifying oldMap while we are
            // iterating over it
            synchronized (oldMap) {
                newMap.putAll(oldMap);
            }
        }

        copyOnInheritThreadLocal.set(newMap);
        return newMap;
    }

    /**
     * Put a context value (the <code>val</code> parameter) as identified with the
     * <code>key</code> parameter into the current thread's context map. Note that
     * contrary to log4j, the <code>val</code> parameter can be null.
     * <p/>
     * <p/>
     * If the current thread does not have a context map it is created as a side
     * effect of this call.
     *
     * @throws IllegalArgumentException in case the "key" parameter is null
     */
    @Override
    public void put(String key, String val) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }

        Map<String, String> oldMap = copyOnInheritThreadLocal.get();
        Integer lastOp = getAndSetLastOperation(WRITE_OPERATION);

        if (wasLastOpReadOrNull(lastOp) || oldMap == null) {
            Map<String, String> newMap = duplicateAndInsertNewMap(oldMap);
            newMap.put(key, val);
        } else {
            oldMap.put(key, val);
        }
    }

    /**
     * Remove the the context identified by the <code>key</code> parameter.
     * <p/>
     */
    @Override
    public void remove(String key) {
        if (key == null) {
            return;
        }
        Map<String, String> oldMap = copyOnInheritThreadLocal.get();
        if (oldMap == null)
            return;

        Integer lastOp = getAndSetLastOperation(WRITE_OPERATION);

        if (wasLastOpReadOrNull(lastOp)) {
            Map<String, String> newMap = duplicateAndInsertNewMap(oldMap);
            newMap.remove(key);
        } else {
            oldMap.remove(key);
        }
    }

    /**
     * Clear all entries in the MDC.
     */
    @Override
    public void clear() {
        lastOperation.set(WRITE_OPERATION);
        copyOnInheritThreadLocal.remove();
    }

    /**
     * Get the context identified by the <code>key</code> parameter.
     * <p/>
     */
    @Override
    public String get(String key) {
        final Map<String, String> map = copyOnInheritThreadLocal.get();
        if ((map != null) && (key != null)) {
            return map.get(key);
        } else {
            return null;
        }
    }

    /**
     * Get the current thread's MDC as a map. This method is intended to be used
     * internally.
     */
    public Map<String, String> getPropertyMap() {
        lastOperation.set(MAP_COPY_OPERATION);
        return copyOnInheritThreadLocal.get();
    }

    /**
     * Returns the keys in the MDC as a {@link Set}. The returned value can be
     * null.
     */
    public Set<String> getKeys() {
        Map<String, String> map = getPropertyMap();

        if (map != null) {
            return map.keySet();
        } else {
            return null;
        }
    }

    /**
     * Return a copy of the current thread's context map. Returned value may be
     * null.
     */
    @Override
    public Map<String, String> getCopyOfContextMap() {
        Map<String, String> hashMap = copyOnInheritThreadLocal.get();
        if (hashMap == null) {
            return null;
        } else {
            return new HashMap<String, String>(hashMap);
        }
    }

    @Override
    public void setContextMap(Map<String, String> contextMap) {
        lastOperation.set(WRITE_OPERATION);

        Map<String, String> newMap = Collections.synchronizedMap(new HashMap<String, String>());
        newMap.putAll(contextMap);

        // the newMap replaces the old one for serialisation's sake
        copyOnInheritThreadLocal.set(newMap);
    }
}