package com.redick.support.mysql;

import static net.logstash.logback.marker.Markers.append;

import com.mysql.cj.MysqlConnection;
import com.mysql.cj.Query;
import com.mysql.cj.interceptors.QueryInterceptor;
import com.mysql.cj.log.Log;
import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.protocol.ServerSession;
import com.redick.util.LogUtil;
import java.util.Properties;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * @author Redick01
 */
@Slf4j
public class Mysql8QueryInterceptor implements QueryInterceptor {

    @Override
    public QueryInterceptor init(MysqlConnection mysqlConnection, Properties properties, Log log) {
        return null;
    }

    @Override
    public <T extends Resultset> T preProcess(Supplier<String> supplier, Query query) {
        String start = String.valueOf(System.currentTimeMillis());
        MDC.put("sql_exec_time", start);
        log.info(LogUtil.customizeMarker(LogUtil.kLOG_KEY_TRACE_TAG, "sql_exec_before"), "开始执行sql");
        return null;
    }

    @Override
    public boolean executeTopLevelOnly() {
        return false;
    }

    @Override
    public void destroy() {

    }

    @Override
    public <T extends Resultset> T postProcess(Supplier<String> supplier, Query query, T t,
            ServerSession serverSession) {
        long start = Long.parseLong(MDC.get("sql_exec_time"));
        long end = System.currentTimeMillis();
        log.info(LogUtil.customizeMarker(LogUtil.kLOG_KEY_TRACE_TAG, "sql_exec_after")
                .and(append(LogUtil.kLOG_KEY_SQL_EXEC_DURATION, end - start)), "结束执行sql");
        return null;
    }
}
