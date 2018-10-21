package org.bumble.core.action.impl.client;

import java.nio.channels.SocketChannel;

import org.bumble.client.condition.BumbleCondition;
import org.bumble.client.condition.BumbleConditionFactory;
import org.bumble.core.BumbleConst;
import org.bumble.core.action.Action;
import org.bumble.core.action.ActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Receive message from manager and notify the client awaiting thread to continue the logic etc...
 * <p>
 * @author shenxiangyu
 *
 */
public abstract class NotifyClientActionService implements ActionService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public void execute(final Action action, SocketChannel channel, Object... param) {
		final String conditionId = action.getParamEntry(BumbleConst.RESP_CONDITION_ID);
		logger.debug("Receive action message to notify condition [" + conditionId + "]");
		
		final BumbleCondition condition = BumbleConditionFactory.getInstance().getCondition(conditionId);
		
		String errMsg = "";
		if (condition == null) {
			errMsg = "Condition [" + conditionId + "] does not exist.";
			logger.error(errMsg);
			logger.trace(errMsg, new Exception(errMsg));
			return;
		}
		
		condition.setState(getState());
		condition.signal();
	}
	
	protected abstract String getState();
}