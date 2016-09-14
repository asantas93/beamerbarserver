package config;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.SQLException;

public class SQLCallInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } catch (SQLException e) {
            throw new RuntimeException(ExceptionUtils.getStackTrace(e), e);
        }
    }

}