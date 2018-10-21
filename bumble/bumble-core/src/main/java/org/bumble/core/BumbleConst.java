package org.bumble.core;

public class BumbleConst {
	/**
	 * Transaction Id
	 */
	public final static String TXN_ID = "txnId";
	
	/**
	 * Client Name
	 */
	public final static String CLIENT_NAME = "clientName";
	
	/**
	 * Client Unique Name
	 */
	public final static String CLIENT_UNIQ_NAME = "clientUniqName";
	
	/**
	 * Transaction state
	 */
	public final static String TXN_STATE = "state";
	
	/**
	 * Transaction Group Id
	 */
	public final static String TXN_GROUP_ID = "txnGroupId";
	
	/**
	 * Transaction Starter Id
	 */
	public final static String STARTER_TXN_ID = "starterTxnId";
	
	/**
	 * Transaction list in a Transaction Group
	 */
	public final static String TXN_LIST = "txnList";
	
	/**
	 * <pre>
	 * Transaction Group growing is stopped or not
	 * if it stopped growing, then no new transaction
	 * can be added to this group
	 * </pre>
	 */
	public final static String TXN_GROUP_STOP_GROW = "txnGroupStopGrow";
	
	/**
	 * <pre>
	 * Lock Condition id sent from client to manager, and after manager logic
	 * processed the manager will send response message with this condition id
	 * to the client, at this stage the client will notify the awaiting thread
	 * to continue the following logic
	 * </pre>
	 */
	public final static String RESP_CONDITION_ID = "respConditionId";
	
	/**
	 * Starter Manager Unique Name
	 */
	public final static String STARTER_MANAGER_UNIQ_NAME = "starterManagerUniqName";
}
