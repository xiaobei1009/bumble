package org.bumble.manager.txn;

import org.bumble.core.BumbleConst;

import com.alibaba.fastjson.JSONObject;

public class Txn {
	public interface State {
		// Transaction is initialized
		String INIT = "init";
		
		// Transaction is committed successfully
		String SUCCESS = "success";
		
		// Transaction commit failed
		String FAIL = "failed";
		
		// Transaction 2nd phase commit successfully
		String SUCCESS_2PHASE ="success-second-phase";
		
		// Transaction 2nd phase commit failed
		String FAIL_2PHASE = "fail-second-phase";
	}
	public Txn(String clientName, String clientUniqName, String txnId) {
		this.clientName = clientName;
		this.setClientUniqName(clientUniqName);
		this.txnId = txnId;
	}
	
	private String clientUniqName = null;
	private String clientName = null;
	private String txnId = null;
	private String state = Txn.State.INIT;
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	
	public String toJsonString() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(BumbleConst.CLIENT_NAME, clientName);
		jsonObj.put(BumbleConst.CLIENT_UNIQ_NAME, clientUniqName);
		jsonObj.put(BumbleConst.TXN_ID, txnId);
		jsonObj.put(BumbleConst.TXN_STATE, state);
		
		return jsonObj.toJSONString();
	}
	public static Txn parseJson(String str) {
		JSONObject jsonObject = (JSONObject) JSONObject.parse(str);
		String clientName = jsonObject.getString(BumbleConst.CLIENT_NAME);
		String clientUniqName = jsonObject.getString(BumbleConst.CLIENT_UNIQ_NAME);
		String txnId = jsonObject.getString(BumbleConst.TXN_ID);
		String state = jsonObject.getString(BumbleConst.TXN_STATE);
		
		Txn txn = new Txn(clientName, clientUniqName, txnId);
		txn.setState(state);
		return txn;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getClientUniqName() {
		return clientUniqName;
	}
	public void setClientUniqName(String clientUniqName) {
		this.clientUniqName = clientUniqName;
	}
}