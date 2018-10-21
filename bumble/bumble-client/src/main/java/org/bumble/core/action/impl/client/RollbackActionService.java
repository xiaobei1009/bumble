package org.bumble.core.action.impl.client;

import org.bumble.client.condition.BumbleCondition;

/**
 * Receive rollback command from bumble manager
 * and notify the awaiting thread to rollback the transaction
 * <p>
 * @author shenxiangyu
 *
 */
public class RollbackActionService extends NotifyClientActionService {

	@Override
	protected String getState() {
		logger.debug("Ready for notify the condition to rollback.");
		// Roll back the transaction
		return BumbleCondition.State.FAIL;
	}

}
