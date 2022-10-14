package com.redick.support.mysql;

import static net.logstash.logback.marker.Markers.append;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementInterceptorV2;
import com.redick.util.LogUtil;
import java.sql.SQLException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * @author Redick01
 */
@Slf4j
public class Mysql5StatementInterceptor implements StatementInterceptorV2 {

    @Override
    public void init(Connection connection, Properties properties) throws SQLException {

    }

    @Override
    public ResultSetInternalMethods preProcess(String s, Statement statement, Connection connection)
            throws SQLException {
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
    public ResultSetInternalMethods postProcess(String s, Statement statement,
            ResultSetInternalMethods resultSetInternalMethods, Connection connection, int i,
            boolean b, boolean b1, SQLException e) throws SQLException {
        long start = Long.parseLong(MDC.get("sql_exec_time"));
        long end = System.currentTimeMillis();
        log.info(LogUtil.customizeMarker(LogUtil.kLOG_KEY_TRACE_TAG, "sql_exec_after")
                .and(append(LogUtil.kLOG_KEY_SQL_EXEC_DURATION, end - start)), "结束执行sql");
        return null;
    }
}
