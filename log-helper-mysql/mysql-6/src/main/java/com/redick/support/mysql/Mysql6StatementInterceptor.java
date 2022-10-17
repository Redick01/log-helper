package com.redick.support.mysql;

import static net.logstash.logback.marker.Markers.append;

import com.mysql.cj.api.MysqlConnection;
import com.mysql.cj.api.jdbc.Statement;
import com.mysql.cj.api.jdbc.interceptors.StatementInterceptor;
import com.mysql.cj.api.log.Log;
import com.mysql.cj.api.mysqla.result.Resultset;
import com.redick.util.LogUtil;
import java.sql.SQLException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * @author Redick01
 */
@Slf4j
public class Mysql6StatementInterceptor implements StatementInterceptor {

    @Override
    public StatementInterceptor init(MysqlConnection mysqlConnection, Properties properties,
            Log log) {
        return null;
    }

    @Override
    public <T extends Resultset> T preProcess(String s, Statement statement) throws SQLException {
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
    public <T extends Resultset> T postProcess(String s, Statement statement, T t, int i, boolean b,
            boolean b1, Exception e) throws SQLException {
        long start = Long.parseLong(MDC.get("sql_exec_time"));
        long end = System.currentTimeMillis();
        log.info(LogUtil.customizeMarker(LogUtil.kLOG_KEY_TRACE_TAG, "sql_exec_after")
                .and(append(LogUtil.kLOG_KEY_SQL_EXEC_DURATION, end - start)), "结束执行sql");
        return null;
    }
}
