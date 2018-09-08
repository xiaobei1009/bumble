package org.bumble.core.action.impl.mngr.grp;

import java.nio.channels.SocketChannel;

import org.bumble.core.action.Action;
import org.bumble.manager.remoting.RemotingTransporterFactory4BMngr;
import org.bumble.manager.txn.Txn;

/**
 * Send rollback message to client
 * This action is processed by a participant manager which does not have the group instance in 
 * memory. And this action is sent from the starter manager when the starter manager notify all
 * the participants to rollback.
 * <p>
 * @author shenxiangyu
 *
 */
public class RollbackActionService extends TxnGroupActionService {

	@Override
	protected void doExecute(String txnGroupId, Txn txn, Action action, SocketChannel channel) {
		logger.debug("Send rollback message to the client: " + txn.toJsonString());
		
		String clientUniqName = txn.getClientUniqName();
		
		RemotingTransporterFactory4BMngr.getInstance().sendMsgToClient(clientUniqName, action);
	}

}
