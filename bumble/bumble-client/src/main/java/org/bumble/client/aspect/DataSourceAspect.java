package org.bumble.client.aspect;

import java.sql.Connection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.bumble.client.conn.BumbleConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* javax.sql.DataSource.getConnection*(..))")
    public Connection around(ProceedingJoinPoint point) throws Throwable{
    	
		// Intercept for each transaction
        logger.debug("getConnection-start---->");
        Connection connection = new BumbleConnection((Connection) point.proceed());
        
        logger.debug("connection-->" + connection);
        logger.debug("getConnection-end---->");
        return connection;
    }

}