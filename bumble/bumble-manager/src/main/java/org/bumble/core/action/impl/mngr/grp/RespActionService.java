package org.bumble.core.action.impl.mngr.grp;

import java.nio.channels.SocketChannel;

import org.bumble.core.action.Action;
import org.bumble.manager.remoting.RemotingTransporterFactory4BMngr;
import org.bumble.manager.txn.Txn;

/**
 * Action Service for transaction group action response send to client
 * <pre>
 * TxnJoinActionService will sent response message from starter manager
 * to the direct manager then the client.
 * This class will manage the redirection from direct manager to client
 * by the call stack.
 * This receives message from starter manager and transport this message
 * to the client. 
 * </pre>
 * @author shenxiangyu
 *
 */
public class RespActionService extends TxnGroupActionService {
	
	protected void doExecute(String txnGroupId, Txn txn, Action action, SocketChannel channel) {
		String uniqName = action.getRespDestination();
		
		logger.debug("Send response message to the client");
		
		// Send the message to client by the call stack
		RemotingTransporterFactory4BMngr.getInstance().sendMsg(uniqName, action);
	};
}