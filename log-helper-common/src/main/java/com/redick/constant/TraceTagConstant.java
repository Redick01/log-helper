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
}
