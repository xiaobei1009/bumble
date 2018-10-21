package org.bumble.client.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.bumble.base.util.UuidUtil;
import org.bumble.client.action.ClientActionBuilder;
import org.bumble.client.condition.BumbleCondition;
import org.bumble.client.condition.BumbleConditionFactory;
import org.bumble.client.remoting.RemotingTransporterFactory4BClient;
import org.bumble.client.threadlocal.TxnThreadLocal;
import org.bumble.core.BumbleConst;
import org.bumble.core.action.ActionConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTransactionInterceptor implements TransactionInterceptor {
	
	protected static Logger logger = LoggerFactory.getLogger(AbstractTransactionInterceptor.class);
	
	public Object intercept(ProceedingJoinPoint point) throws Exception {
		Object obj = null;
		
		RemotingTransporterFactory4BClient client = RemotingTransporterFactory4BClient.getInstance();
		
		// If the bumble is disabled, then process the transaction directly
		// Service Degradation if no bumble manager is provided
		if (!client.isConnected()) {
			try {
				obj = point.proceed();
			} catch (Throwable e) {
				logger.error(e.getMessage());
				logger.trace(e.getMessage(), e);
			}
			return obj;
		}
		
		// Generate a condition for this transaction
		BumbleCondition condition = BumbleConditionFactory.getInstance().newCondition();
		String txnId = condition.getId();
		
		// Get transaction group id from proceeding context
		String txnGroupId = getTxnGroupId();
		
		String actionType = null;
		Boolean isStarter = false;
		String starterManagerUniqName = null;
		if (txnGroupId == null) {
			txnGroupId = UuidUtil.uuid();
			actionType = ActionConst.Type.TXN_START;
			starterManagerUniqName = client.getManagerUniqName();
			isStarter = true;
			logger.debug("Start a new transaction group: " + txnGroupId);
		} else {
			actionType = ActionConst.Type.TXN_JOIN;
			starterManagerUniqName = this.getStarterManagerUniqName();
			logger.debug("Joining in the transaction group: " + txnGroupId);
		}
		
		TxnThreadLocal current = new TxnThreadLocal(txnId, txnGroupId);
		TxnThreadLocal.setCurrent(current);
		current.setStarterManagerUniqName(starterManagerUniqName);
		
		// response condition for synchronize message
		BumbleCondition respCondition = BumbleConditionFactory.getInstance().newCondition();
		
		// Tell manager start a new transaction group or join in 
		// an existing transaction group
		String startOrJoinMsg = buildActionMsg(actionType, BumbleConst.RESP_CONDITION_ID, respCondition.getId());
		client.sendMsg(startOrJoinMsg);
		
		// Wait for response of manager indicating new or join a group successfully
		logger.debug("Waiting for " + (isStarter ? "new" : "join") + " transaction group process by managaer with response condition id: " + respCondition.getId());
		try {
			respCondition.await();
		} catch (Exception e) {
			// Send error message to manager when await failed
			if (isStarter) {
				String endMsg = buildActionMsg(ActionConst.Type.TXN_END);
				client.sendMsg(endMsg);
			} else {
				String failMsg = buildActionMsg(ActionConst.Type.TXN_FAIL);
				client.sendMsg(failMsg);
			}
			return null;
		} finally {
			BumbleConditionFactory.getInstance().remove(respCondition);
		}
		
		try {
			obj = point.proceed();
		} catch (Throwable e) {
			// The fail message will be sent to manager by BumbleConnection#rollback()
			// Record error log
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
		
		if (isStarter) {
			// Send message to manager check all the state of the transaction participants
			String endMsg = buildActionMsg(ActionConst.Type.TXN_END);
			client.sendMsg(endMsg);
		}
		
		return obj;
	}
	
	private String buildActionMsg(String actionType, Object... params) {
		return ClientActionBuilder.getInstance().buildActionMsg(actionType, params);
	}
	
	protected abstract String getTxnGroupId();
	
	protected abstract String getStarterManagerUniqName();
}
