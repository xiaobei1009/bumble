package org.bumble.core.action.impl.mngr.grp;

import java.nio.channels.SocketChannel;
import java.util.Map;

import org.bumble.core.BumbleConst;
import org.bumble.core.action.Action;
import org.bumble.core.action.ActionService;
import org.bumble.manager.txn.Txn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Action Service for transaction group action
 * <p>
 * Receive transaction group relative parameters before execution
 * <p>
 * @author shenxiangyu
 *
 */
public abstract class TxnGroupActionService implements ActionService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public void execute(Action action, SocketChannel channel, Object... param) {
		logger.debug("Receive action: " + action.getName());
		
		Map<String, Object> params = action.getParam();
		String txnGroupId = (String) params.get(BumbleConst.TXN_GROUP_ID);
		
		String txnId = (String) params.get(BumbleConst.TXN_ID);
		String clientName = (String) params.get(BumbleConst.CLIENT_NAME);
		String clientUniqName = (String) params.get(BumbleConst.CLIENT_UNIQ_NAME);
		
		Txn txn = new Txn(clientName, clientUniqName, txnId);
		doExecute(txnGroupId, txn, action, channel);
	}
	
	/**
	 * Do action for transaction group relative message
	 * <p>
	 * @param txnGroupId
	 * @param txn
	 * @param action
	 */
	protected abstract void doExecute(String txnGroupId, Txn txn, Action action, SocketChannel channel);
}