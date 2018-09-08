package org.bumble.core.action.impl.mngr.grp.redirect;

import org.bumble.core.action.Action;
import org.bumble.manager.txn.Txn;
import org.bumble.manager.txn.TxnGroupFactory;

/**
 * Receive message from client that the second phase transaction in that
 * transaction group process failed.
 * <p>
 * @author shenxiangyu
 *
 */
public class TxnFailSecondPhaseActionService extends RedirectTxnGroupActionService {

	@Override
	protected void executeAction(String txnGroupId, Txn txn, Action action) {
		txn.setState(Txn.State.FAIL_2PHASE);
		TxnGroupFactory.getInstance().addUpdateTxn(txnGroupId, txn, action);
	}
}