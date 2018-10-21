package org.bumble.core.action.impl.mngr.grp.redirect;

import java.nio.channels.SocketChannel;

import org.bumble.base.BumbleNameBuilder;
import org.bumble.core.BumbleConst;
import org.bumble.core.action.Action;
import org.bumble.core.action.impl.mngr.grp.TxnGroupActionService;
import org.bumble.manager.remoting.RemotingTransporterFactory4BMngr;
import org.bumble.manager.txn.Txn;
import org.bumble.manager.txn.TxnGroup;
import org.bumble.manager.txn.TxnGroupFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Action Service for messages from client to starter manager
 * <pre>
 * If Client is transaction group starter
 *   CLIENT -> DIRECT-MANAGER(STARTER-MANAGER)
 * 
 * If Client is transaction group participant
 *   CLIENT -> DIRECT-MANAGER -> STARTER-MANAGER
 *   
 * In the end the transaction group starter manager will 
 * process the action.
 * 
 * </pre>
 * <p>
 * @author shenxiangyu
 *
 */
public abstract class RedirectTxnGroupActionService extends TxnGroupActionService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * <pre>
	 * Check if the it is the starter manager of the received action
	 * a> If it is the starter manager, then execute the action
	 * b> If not, redirect this message to the starter manager
	 * 
	 * </pre>
	 * <p>
	 * @param txnGroupId
	 * @param txn
	 * @param action
	 * @param channel
	 */
	protected void doExecute(String txnGroupId, Txn txn, Action action, SocketChannel channel) {
		String starterManagerUniqName = action.getParamEntry(BumbleConst.STARTER_MANAGER_UNIQ_NAME);
		String thisManagerUniqName = BumbleNameBuilder.getInstance().getUniqName();
		
		if (starterManagerUniqName.equals(thisManagerUniqName)) {
			TxnGroup txnGroup = TxnGroupFactory.getInstance().getGroup(txnGroupId);
			txnInGroup = txnGroup.getTxns().get(txn.getTxnId());
			executeAction(txnGroupId, txn, action);
		} else {
			action.addCallstack();
			RemotingTransporterFactory4BMngr.getInstance().sendMsgToMngr(starterManagerUniqName, action);
		}
	};
	
	protected Txn txnInGroup = null;
	
	protected abstract void executeAction(String txnGroupId, Txn txn, Action action);
}