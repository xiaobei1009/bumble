package org.bumble.client.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.bumble.base.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * Aspect of bumble transaction
 * <p>
 * There are two different way of launching the bumble transaction.
 * 1. Place a BumbleTx as an annotation on any method which needs cluster transaction
 * 2. Implements IBumbleTx interface, and all the public method in the implementation class will be wrapped with Bumble transaction
 * 
 * @author shenxiangyu
 *
 */
@Aspect
@Component
public class TransactionAspect implements Ordered {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Around("@annotation(org.bumble.client.BumbleTxn)")
    public Object proceedAnnotationJoinPoint(ProceedingJoinPoint point) throws Throwable {
        logger.debug("Annotation-bumble-txn-start---->");
        
        TransactionInterceptor transactionInterceptor = ReflectUtil.getInstance(TransactionInterceptor.class);
        Object obj = transactionInterceptor.intercept(point);
        
        logger.debug("Annotation-bumble-txn-end---->");
        return obj;
    }

    @Around("this(org.bumble.client.IBumbleTxn) && execution(public * *(..))")
    public Object proceedInterfaceJoinPoint(ProceedingJoinPoint point) throws Throwable {
        logger.debug("Interface-bumble-txn-start---->");
        
        TransactionInterceptor transactionInterceptor = ReflectUtil.getInstance(TransactionInterceptor.class);
        Object obj = transactionInterceptor.intercept(point);
        
        logger.debug("Interface-bumble-txn-end---->");
        return obj;
    }
    
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
