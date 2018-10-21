package org.bumble.core.action.impl.mngr.grp;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import org.bumble.base.util.NioSocketUtil;
import org.bumble.core.BumbleConst;
import org.bumble.core.action.Action;
import org.bumble.core.action.ActionConst;
import org.bumble.manager.txn.Txn;
import org.bumble.manager.txn.TxnGroupFactory;

/**
 * Add new transaction group
 * <pre>
 * This is coming form starter transaction group client and processed by the
 * directly connected starter transaction group manager.
 * 
 * For synchronize process, a response message will be sent to the client to
 * notify the client awaiting thread to awake.
 * </pre>
 * @author shenxiangyu
 *
 */
public class TxnStartActionService extends TxnGroupActionService {

	@Override
	protected void doExecute(String txnGroupId, Txn txn, Action action, SocketChannel channel) {
		logger.debug("New trasnaction group [" + txnGroupId + "] txnId: " + txn.getTxnId());
		TxnGroupFactory.getInstance().newTxnGroup(txnGroupId, txn);
		
		String respConditionId = action.getParamEntry(BumbleConst.RESP_CONDITION_ID);
		
		Action respAction = new Action(ActionConst.Type.RESP);
		respAction.setParamEntry(BumbleConst.RESP_CONDITION_ID, respConditionId);
		
		try {
			// Send response message directly to the starter client
			NioSocketUtil.sendMsg(channel, respAction.toJsonString());
		} catch (IOException e) {
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
	}

}
