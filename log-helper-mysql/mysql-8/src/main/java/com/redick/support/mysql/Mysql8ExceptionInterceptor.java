package com.redick.support.mysql;

import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.log.Log;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Redick01
 */
@Slf4j
public class Mysql8ExceptionInterceptor implements ExceptionInterceptor {

    @Override
    public ExceptionInterceptor init(Properties properties, Log log) {
        String queryInterceptors = properties.getProperty("queryInterceptors");
        if (queryInterceptors == null ||
                !queryInterceptors.contains(Mysql8ExceptionInterceptor.class.getName())) {
            throw new IllegalStateException(
                    "TracingQueryInterceptor must be enabled to use TracingExceptionInterceptor.");
        }
        return new Mysql8ExceptionInterceptor();
    }

    @Override
    public void destroy() {

    }

    @Override
    public Exception interceptException(Exception e) {
        return null;
    }
}
