package org.bumble.core.action;

public interface ActionConst {
	String NAME = "action";
	String FROM = "from";
	String CALLSTACK = "callstack";
	String PARAM = "param";
	
	interface Type {
		//——————————————————————————————————————————————————————————————————————————————
		/**
		 * Send from manager to itself to notify the manager to exit
		 */
		String SHUTDOWN = "shutdown";
		//——————————————————————————————————————————————————————————————————————————————
		/**
		 * Send from client to manager when it connect successfully
		 */
		String CONNECT_SUCCESS = "connect-success";
		
		//——————————————————————————————————————————————————————————————————————————————
		/**
		 * Send from client to manager or manager as a client to manager as a server
		 */
		String HEART_REQ = "heart-req";
		
		/**
		 * Send from manager to client or manager as a server to manager as a client
		 */
		String HEART_RESP = "heart-resp";
		
		//——————————————————————————————————————————————————————————————————————————————
		/**
		 * <pre>
		 * Send from manager to client when all the first phase commit succeed
		 * and tell all the transaction clients in this transaction group to commit
		 * </pre>
		 */
		String COMMIT = "commit";
		
		/**
		 * <pre>
		 * Send from manager to client when any of the first phase commit failed
		 * and tell all the transaction clients in this transaction group to roll-back
		 * </pre>
		 */
		String ROLLBACK = "rollback";
		
		/**
		 * <pre>
		 * Send from manager to client when a compensate is needed
		 * (The second commit is succeeded, but the send commit on other service
		 * in this transaction group is failed)
		 * </pre>
		 */
		String COMPENSATE = "compensate";
		
		//——————————————————————————————————————————————————————————————————————————————
		/**
		 * <pre>
		 * Send from client to manager when it is the starter of the transaction group
		 * Send at the beginning of the transaction group process
		 * </pre>
		 */
		String TXN_START = "txn-start";
		
		/**
		 * Send from manager to client
		 */
		String RESP = "resp";
		
		/**
		 * <pre>
		 * Send from client to manager when it is the starter of the transaction group
		 * Send at the end of the transaction group process
		 * </pre>
		 */
		String TXN_END = "txn-end";
		
		/**
		 * Send from client to manager when it is joining in a transaction group
		 */
		String TXN_JOIN = "txn-join";
		
		/**
		 * Send from client to manager when the first phase commit is succeeded
		 */
		String TXN_SUCCESS = "txn-success";
		
		/**
		 * Send from client to manager when the first phase commit is failed
		 */
		String TXN_FAIL = "txn-fail";
		
		/**
		 * Send from client to manager when the second phase commit is succeeded
		 */
		String TXN_SUCCESS_2PHASE = "txn-success-second-phase";
		
		/**
		 * Send from client to manager when the second phase commit is failed
		 */
		String TXN_FAIL_2PHASE = "txn-fail-second-phase";
		
		/**
		 * Send from client to manager when the compensate is succeeded
		 */
		String TXN_SUCCESS_COMPENSATE = "txn-success-compensate";
		
		/**
		 * Send from client to manager when the compensate is failed
		 */
		String TXN_FAIL_COMPENSATE = "txn-fail-compensate";
	}
	
	interface Param {
		String REQ_FROM_UNIQ_NAME = "request-from-uniq-name";
	}
}
