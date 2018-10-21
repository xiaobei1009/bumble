package org.bumble.core.action.impl.client;

import org.bumble.client.condition.BumbleCondition;

/**
 * Receive response action from bumble manager
 * <p>
 * This is used to as a synchronous mechanism for socket asynchronous processing
 * E.g. The thread will wait for the response message of bumble manager transaction 
 *     group create complete. So the join transaction group action will be triggered
 *     after the create transaction group process.
 * <p>
 * @author shenxiangyu
 *
 */
public class RespActionService extends NotifyClientActionService {
	
	@Override
	protected String getState() {
		return BumbleCondition.State.SUCCESS;
	}
	
}
