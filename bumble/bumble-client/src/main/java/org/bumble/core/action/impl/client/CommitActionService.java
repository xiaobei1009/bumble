package org.bumble.core.action.impl.client;

import org.bumble.client.condition.BumbleCondition;

/**
 * Receive commit command from bumble manager
 * and notify the awaiting thread to commit the transaction
 * <p>
 * @author shenxiangyu
 *
 */
public class CommitActionService extends NotifyClientActionService {
	
	@Override
	protected String getState() {
		logger.debug("Ready for notify the condition to commit.");
		// Commit the transaction
		return BumbleCondition.State.SUCCESS;
	}
}
