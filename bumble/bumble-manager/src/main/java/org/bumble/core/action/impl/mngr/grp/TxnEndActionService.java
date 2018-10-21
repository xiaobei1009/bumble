package org.bumble.core.action.impl.mngr.grp;

import java.nio.channels.SocketChannel;

import org.bumble.core.action.Action;
import org.bumble.manager.txn.Txn;
import org.bumble.manager.txn.TxnGroupFactory;

/**
 * This action message is sent from the starter client after all the business logics are processed.
 * This starter bumble manager will check if all the participants are ready to commit.
 * <p>
 * @author shenxiangyu
 *
 */
public class TxnEndActionService extends TxnGroupActionService {

	@Override
	protected void doExecute(String txnGroupId, Txn txn, Action action, SocketChannel channel) {
		logger.debug("End of transaction group [" + txnGroupId + "], check all participants state.");
		TxnGroupFactory.getInstance().finish(txnGroupId);
	}
}

