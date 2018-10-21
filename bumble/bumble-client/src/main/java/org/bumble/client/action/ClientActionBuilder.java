package org.bumble.client.action;

import org.bumble.base.BumbleNameBuilder;
import org.bumble.client.threadlocal.TxnThreadLocal;
import org.bumble.core.BumbleConst;
import org.bumble.core.action.Action;

public class ClientActionBuilder {
	private volatile static ClientActionBuilder instance = null;
	public static ClientActionBuilder getInstance() {
		if (instance == null) {
			instance = new ClientActionBuilder();
		}
		return instance;
	}
	public String buildActionMsg(String actionType) {
		return buildActionMsg(actionType, new Object[] {});
	}
	public String buildActionMsg(String actionType, Object... params) {
		TxnThreadLocal current = TxnThreadLocal.getCurrent();
		String txnGroupId = current.getTxnGroupId();
		String txnId = current.getTxnId();
		String starterManagerUniqName = current.getStarterManagerUniqName();
		String clientName = BumbleNameBuilder.getInstance().getName();
		String clientUniqName = BumbleNameBuilder.getInstance().getUniqName();
		
		Action action = new Action(actionType);
		action.addCallstack();
		action.setParamEntry(BumbleConst.TXN_GROUP_ID, txnGroupId);
		action.setParamEntry(BumbleConst.TXN_ID, txnId);
		action.setParamEntry(BumbleConst.STARTER_MANAGER_UNIQ_NAME, starterManagerUniqName);
		action.setParamEntry(BumbleConst.CLIENT_NAME, clientName);
		action.setParamEntry(BumbleConst.CLIENT_UNIQ_NAME, clientUniqName);
		
		for (int i = 0; i < params.length; i += 2) {
			String key = (String) params[i];
			String value = (String) params[i + 1];
			action.setParamEntry(key, value);
		}
		
		String msg = action.toJsonString();
		return msg;
	}
}
