package org.bumble.core.action.impl.mngr.grp.redirect;

import org.bumble.core.action.Action;
import org.bumble.core.action.ActionConst;
import org.bumble.manager.remoting.RemotingTransporterFactory4BMngr;
import org.bumble.manager.txn.Txn;
import org.bumble.manager.txn.TxnGroupFactory;

/**
 * Receive message from client that the a new transaction is coming to 
 * add to a transaction group.
 * <p>
 * @author shenxiangyu
 *
 */
public class TxnJoinActionService extends RedirectTxnGroupActionService {

	@Override
	protected void executeAction(String txnGroupId, Txn txn, Action action) {
		logger.debug("Join in trasnaction group [" + txnGroupId + "] txnId: " + txn.getTxnId());
		TxnGroupFactory.getInstance().addUpdateTxn(txnGroupId, txn, action);
		
		Action respAction = Action.parseJson(action.toJsonString());
		respAction.setName(ActionConst.Type.RESP);
		String uniqName = respAction.getRespDestination();
		
		// Send response message from starter manager to the direct manager first
		// by the call stack
		RemotingTransporterFactory4BMngr.getInstance().sendMsg(uniqName, respAction);
	}

}
