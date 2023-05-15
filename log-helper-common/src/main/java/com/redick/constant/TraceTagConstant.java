package com.redick.constant;

/**
 * 标签定义
 *
 * @author Redick01
 */
public class TraceTagConstant {

    public static final String START_TIME = "start_time";

    /**
     * MySQL TAG
     */
    public static final String SQL_EXEC_BEFORE = "sql_exec_before";

    public static final String SQL_EXEC_AFTER = "sql_exec_after";

    /**
     * 当前节点处理中结束TAG
     */
    public static final String ENDPOINT_DONE = "endpoint_done";

    /**
     * Forest拦截器执行标签
     */
    public static final String FOREST_EXEC_BEFORE = "forest_exec_before";

    public static final String FOREST_EXEC_AFTER = "forest_exec_after";

    /**
     * HttpClient拦截器执行标签
     */
    public static final String HTTP_CLIENT_EXEC_BEFORE = "http_client_exec_before";

    public static final String HTTP_CLIENT_EXEC_AFTER = "http_client_exec_after";

    /**
     * OKHTTP Http客户端拦截器执行标签
     */
    public static final String OKHTTP_CLIENT_EXEC_BEFORE = "okhttp_client_exec_before";

    public static final String OKHTTP_CLIENT_EXEC_AFTER = "okhttp_client_exec_after";

    /**
     * RestTemplate 标签
     */
    public static final String REST_TEMPLATE_EXEC_BEFORE = "rest_template_exec_before";

    public static final String REST_TEMPLATE_EXEC_AFTER = "rest_template_exec_after";

    /**
     * Dubbo RPC 调用标签
     */
    public static final String DUBBO_INVOKE_BEFORE = "dubbo_invoke_before";

    public static final String DUBBO_INVOKE_AFTER = "dubbo_invoke_after";

    /**
     * Motan RPC 调用标签
     */
    public static final String MOTAN_CALL_BEFORE = "motan_call_before";

    public static final String MOTAN_CALL_AFTER = "motan_call_after";

    /**
     * GRPC 调用标签
     */
    public static final String GRPC_INVOKE_BEFORE = "grpc_invoke_before";

    public static final String GRPC_INVOKE_AFTER = "grpc_invoke_after";

    /**
     * Spring Cloud Gateway tag
     */
    public static final String SCG_INVOKE_START = "scg_invoke_start";

    public static final String SCG_INVOKE_END = "scg_invoke_end";

    /**
     * Open Feign tag.
     */
    public static final String OPEN_FEIGN_INVOKE_BEFORE = "open_feign_invoke_before";

    public static final String OPEN_FEIGN_INVOKE_AFTER = "open_feign_invoke_after";

    /**
     * Spring Cloud Stream tag.
     */
    public static final String SCS_INVOKE_BEFORE = "scs_invoke_before";

    public static final String SCS_INVOKE_AFTER = "scs_invoke_after";
}
