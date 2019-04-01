package org.bumble.base;

/**
 * Common Callback Interface
 * <p>
 * @author shenxiangyu
 *
 */
public interface Callback {
	/**
	 * Execute the callback method
	 * <p>
	 * @param params
	 */
	Object doCallback(Object... params);
}
