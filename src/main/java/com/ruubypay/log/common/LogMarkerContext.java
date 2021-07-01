package com.ruubypay.log.common;

/**
 * Log上下文
 * @author liupenghui
 * @date 2021/4/27 3:27 下午
 */
public class LogMarkerContext {

    private static boolean enableCalcInvokeTime = false;

    private static InheritableThreadLocal<String> sessionIds = new InheritableThreadLocal<>();

    public static boolean isEnableCalcInvokeTime() {
        return enableCalcInvokeTime;
    }

    public static void setEnableCalcInvokeTime(boolean enableCalcInvokeTime) {
        LogMarkerContext.enableCalcInvokeTime = enableCalcInvokeTime;
    }

    public static String getSessionId() {
        return sessionIds.get();
    }

    public static void removeSessionId() {
        sessionIds.remove();
    }

    public static void setSessionId(String sessionId) {
        sessionIds.set(sessionId);
    }
}
