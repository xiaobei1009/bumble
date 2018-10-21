package org.bumble.manager.txn;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.bumble.base.BasicConst;
import org.bumble.core.BumbleConst;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Transaction Group
 * @author shenxiangyu
 *
 */
public class TxnGroup {
	public TxnGroup(String txnGroupId) {
		this.txnGroupId = txnGroupId;
	}
	public TxnGroup(String txnGroupId, Txn txn) {
		addUpdateTxn(txn);
		this.starterTxnId = txn.getTxnId();
		this.txnGroupId = txnGroupId;
	}
	
	// Transaction Group Id
	private String txnGroupId = null;
	
	// Transaction starter Id
	private String starterTxnId = null;
	
	// Timer
	private Timer timer = new Timer();
	
	// Indicate if the timer is started
	private Boolean timerStarted = false;
	
	// Transaction end check timeout
	private int tranEndCheckTimeout = 10000;
	
	/**
	 * <pre>
	 * When {@link TxnGroupFactory#finish(String)}, 
	 * if not all the transaction participants are ready, 
	 * then it will start a timer with a timeout.
	 * 
	 * This is the identifier of this timeout state
	 * </pre>
	 */
	private Boolean _isWaitingParticipantsStateTimeout = false;
	
	/**
	 * <pre>
	 * Indicate if the group grow is stopped
	 * 
	 * When it is stopped, then the child transaction number will be fixed, 
	 * and no new transaction will be joined into this Transaction Group
	 * </pre>
	 */
	private Boolean _isGrowingStopped = false;
	
	/**
	 * {@link TxnGroup#_isWaitingParticipantsStateTimeout}
	 * @return
	 */
	public Boolean isWaitingParticipantsStateTimeout() {
		return _isWaitingParticipantsStateTimeout;
	}
	
	/**
	 * {@link TxnGroup#_isGrowingStopped}
	 * @return
	 */
	public Boolean isGrowingStopped() {
		return _isGrowingStopped;
	}
	
	/**
	 * <pre>
	 * When {@link TxnGroupFactory#finish(String)}
	 * 
	 * If not all the transaction participants are ready, then this
	 * failure count down will be started by a timer with a timeout.
	 * 
	 * When the timeout period passed, this transaction group will be
	 * marked as {@link TxnGroup#isWaitingParticipantsStateTimeout}
	 * and rollback message will be sent to all the transaction participants.
	 * 
	 * </pre>
	 */
	public synchronized void startFailCountDown() {
		final TxnGroup _this = this;
		
		if (timerStarted)
			timer.cancel();
		
		timerStarted = true;
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				_this._isWaitingParticipantsStateTimeout = true;
				
				TxnGroupFactory.getInstance().finish(_this.txnGroupId);
			}
			
		}, tranEndCheckTimeout);
	}
	
	/**
	 * <pre>
	 * The Transaction Group will stop growing
	 * 
	 * It means no new transaction will be joined into this Transaction Group
	 * </pre>
	 */
	public void stopGrowing() {
		_isGrowingStopped = true;
	}
	
	public String getStarterTxnId() {
		return starterTxnId;
	}
	public void setStarterTxnId(String starterTxnId) {
		this.starterTxnId = starterTxnId;
	}

	private Map<String, Txn> txns = new HashMap<String, Txn>();
	
	public Map<String, Txn> getTxns() {
		return txns;
	}
	
	/**
	 * Only the {@link org.bumble.manager.txn.Txn#state} can be updated
	 * @param txn
	 */
	public void addUpdateTxn(Txn txn) {
		if (txns.containsKey(txn.getTxnId())) {
			Txn orgTxn = txns.get(txn.getTxnId());
			orgTxn.setState(txn.getState());
		} else {
			if (_isGrowingStopped) {
				return;
			}
			txns.put(txn.getTxnId(), txn);
		}
	}
	
	public String toJsonString() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(BumbleConst.TXN_GROUP_ID, txnGroupId);
		jsonObj.put(BumbleConst.STARTER_TXN_ID, starterTxnId);
		jsonObj.put(BumbleConst.TXN_GROUP_STOP_GROW, this._isGrowingStopped ? BasicConst.YES : BasicConst.NO);
		JSONArray jsonArray = new JSONArray();
		for (String txnId : txns.keySet()) {
			Txn txn = txns.get(txnId);
			jsonArray.add(txn.toJsonString());
		}
		jsonObj.put(BumbleConst.TXN_LIST, jsonArray);
		return jsonObj.toJSONString();
	}
	
	public static TxnGroup parseJson(String str) {
		JSONObject jsonObj = JSONObject.parseObject(str);
		String txnGroupId = jsonObj.getString(BumbleConst.TXN_GROUP_ID);
		String starterTxnId = jsonObj.getString(BumbleConst.STARTER_TXN_ID);
		String txnGroupStopGrowStr = jsonObj.getString(BumbleConst.TXN_GROUP_STOP_GROW);
		
		TxnGroup txnGroup = new TxnGroup(txnGroupId);
		
		JSONArray jsonArr = jsonObj.getJSONArray(BumbleConst.TXN_LIST);
		for (Object obj : jsonArr) {
			Txn txn = Txn.parseJson((String) obj);
			txnGroup.addUpdateTxn(txn);
		}
		txnGroup.setStarterTxnId(starterTxnId);
		if (txnGroupStopGrowStr.equals(BasicConst.YES)) {
			txnGroup.stopGrowing();
		}
		
		return txnGroup;
	}
	public int getTranEndCheckTimeout() {
		return tranEndCheckTimeout;
	}
	public void setTranEndCheckTimeout(int tranEndCheckTimeout) {
		this.tranEndCheckTimeout = tranEndCheckTimeout;
	}
}
