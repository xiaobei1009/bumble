package org.bumble.core.action.impl.mngr.grp.redirect;

import org.bumble.core.action.Action;
import org.bumble.manager.txn.Txn;
import org.bumble.manager.txn.TxnGroup;
import org.bumble.manager.txn.TxnGroupFactory;

/**
 * Receive message from client that the first phase transaction in that
 * transaction group process failed.
 * <p>
 * @author shenxiangyu
 *
 */
public class TxnFailActionService extends RedirectTxnGroupActionService {

	@Override
	protected void executeAction(String txnGroupId, Txn txn, Action action) {
		logger.debug("Transaction txnId [" + txn.getTxnId() + "] first phase commit failed in transaction group [" + txnGroupId + "]");
		txn.setState(Txn.State.FAIL);
		
		TxnGroupFactory factory = TxnGroupFactory.getInstance();
		factory.addUpdateTxn(txnGroupId, txn, action);
		
		TxnGroup txnGroup = factory.getGroup(txnGroupId);
		if (txnGroup.isGrowingStopped()) {
			factory.finish(txnGroupId);
		}
	}
}