package org.bumble.core.action;

public interface ActionConst {
	public static final String NAME = "action";
	public static final String FROM = "from";
	public static final String CALLSTACK = "callstack";
	public static final String PARAM = "param";
	
	public interface Type {
		//——————————————————————————————————————————————————————————————————————————————
		/**
		 * Send from manager to itself to notify the manager to exit
		 */
		public static final String SHUTDOWN = "shutdown";
		//——————————————————————————————————————————————————————————————————————————————
		/**
		 * Send from client to manager when it connect successfully
		 */
		public static final String CONNECT_SUCCESS = "connect-success";
		
		//——————————————————————————————————————————————————————————————————————————————
		/**
		 * Send from client to manager or manager as a client to manager as a server
		 */
		public static final String HEART_REQ = "heart-req";
		
		/**
		 * Send from manager to client or manager as a server to manager as a client
		 */
		public static final String HEART_RESP = "heart-resp";
		
		//——————————————————————————————————————————————————————————————————————————————
		/**
		 * <pre>
		 * Send from manager to client when all the first phase commit succeed
		 * and tell all the transaction clients in this transaction group to commit
		 * </pre>
		 */
		public static final String COMMIT = "commit";
		
		/**
		 * <pre>
		 * Send from manager to client when any of the first phase commit failed
		 * and tell all the transaction clients in this transaction group to roll-back
		 * </pre>
		 */
		public static final String ROLLBACK = "rollback";
		
		/**
		 * <pre>
		 * Send from manager to client when a compensate is needed
		 * (The second commit is succeeded, but the send commit on other service
		 * in this transaction group is failed)
		 * </pre>
		 */
		public static final String COMPENSATE = "compensate";
		
		//——————————————————————————————————————————————————————————————————————————————
		/**
		 * <pre>
		 * Send from client to manager when it is the starter of the transaction group
		 * Send at the beginning of the transaction group process
		 * </pre>
		 */
		public static final String TXN_START = "txn-start";
		
		/**
		 * Send from manager to client
		 */
		public static final String RESP = "resp";
		
		/**
		 * <pre>
		 * Send from client to manager when it is the starter of the transaction group
		 * Send at the end of the transaction group process
		 * </pre>
		 */
		public static final String TXN_END = "txn-end";
		
		/**
		 * Send from client to manager when it is joining in a transaction group
		 */
		public static final String TXN_JOIN = "txn-join";
		
		/**
		 * Send from client to manager when the first phase commit is succeeded
		 */
		public static final String TXN_SUCCESS = "txn-success";
		
		/**
		 * Send from client to manager when the first phase commit is failed
		 */
		public static final String TXN_FAIL = "txn-fail";
		
		/**
		 * Send from client to manager when the second phase commit is succeeded
		 */
		public static final String TXN_SUCCESS_2PHASE = "txn-success-second-phase";
		
		/**
		 * Send from client to manager when the second phase commit is failed
		 */
		public static final String TXN_FAIL_2PHASE = "txn-fail-second-phase";
		
		/**
		 * Send from client to manager when the compensate is succeeded
		 */
		public static final String TXN_SUCCESS_COMPENSATE = "txn-success-compensate";
		
		/**
		 * Send from client to manager when the compensate is failed
		 */
		public static final String TXN_FAIL_COMPENSATE = "txn-fail-compensate";
	}
	
	public interface Param {
		public static final String REQ_FROM_UNIQ_NAME = "request-from-uniq-name";
	}
}
