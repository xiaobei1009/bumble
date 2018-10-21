package org.bumble.client.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Transaction Interceptor
 * <p>
 * @author shenxiangyu
 *
 */
public interface TransactionInterceptor {
	/**
	 * Wrap the transaction
	 * <p>
	 * @param point
	 * @return
	 * @throws Throwable
	 */
	public Object intercept(ProceedingJoinPoint point) throws Throwable;
}
