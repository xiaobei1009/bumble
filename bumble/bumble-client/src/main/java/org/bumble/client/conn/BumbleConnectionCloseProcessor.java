package org.bumble.client.conn;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

import org.bumble.client.action.ClientActionBuilder;
import org.bumble.client.condition.BumbleCondition;
import org.bumble.client.condition.BumbleConditionFactory;
import org.bumble.client.remoting.RemotingTransporterFactory4BClient;
import org.bumble.client.threadlocal.TxnThreadLocal;
import org.bumble.core.action.ActionConst;
import org.bumble.core.thread.ThreadExecutorGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * The {@link java.sql.Connection} is proxied 
 * by {@link org.bumble.client.conn.BumbleConnection}
 * And the close connection will be processed by this class.
 * 
 * And {@link org.bumble.client.conn.BumbleConnectionCloseProcessor#process(Connection)} 
 * will be called 
 * by {@link org.bumble.client.conn.BumbleConnection#close()}
 * 
 * </pre>
 * @author shenxiangyu
 *
 */
public class BumbleConnectionCloseProcessor {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static BumbleConnectionCloseProcessor instance = null;
	public static BumbleConnectionCloseProcessor getInstance() {
		if (instance == null) {
			instance = new BumbleConnectionCloseProcessor();
		}
		return instance;
	}
	
	private ExecutorService threadPool = ThreadExecutorGenerator.getInstance().getExecutor();
	
	private String prepareActionString(String actionType) {
		String ret = ClientActionBuilder.getInstance().buildActionMsg(actionType);
		return ret;
	}
	
	/**
	 * Process the close connection
	 * @param conn
	 */
	public void process(final Connection conn) {
		TxnThreadLocal current = TxnThreadLocal.getCurrent();
		final String txnId = current.getTxnId();
		final BumbleCondition condition = BumbleConditionFactory.getInstance().getCondition(txnId);
		final RemotingTransporterFactory4BClient client = RemotingTransporterFactory4BClient.getInstance();
		final String successMsg2ndPhase = prepareActionString(ActionConst.Type.TXN_SUCCESS_2PHASE);
		final String faileMsg2ndPhase = prepareActionString(ActionConst.Type.TXN_FAIL_2PHASE);
		
		threadPool.execute(new Runnable() {
			public void run() {
				logger.debug("Awaiting on condition [" + txnId + "], waiting for signal to commit/rollback...");
				try {
					if (condition == null) {
						throw new Exception("Condition [" + txnId + "] does not exist.");
					}
					condition.await();
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.trace(e.getMessage(), e);
					condition.setState(BumbleCondition.State.FAIL);
				} finally {
					BumbleConditionFactory.getInstance().remove(txnId);
				}
				
				String state = condition.getState();
				if (state.equals(BumbleCondition.State.SUCCESS)) {
					try {
						logger.debug("Commit the transaction");
						conn.commit();
						
						// Send 2nd phase commit succeeded message to manager
						client.sendMsg(successMsg2ndPhase);
					} catch (SQLException e) {
						logger.error(e.getMessage());
						logger.trace(e.getMessage(), e);
						
						// Send 2nd phase commit failed message to manager
						client.sendMsg(faileMsg2ndPhase);
					}
				} else if (state.equals(BumbleCondition.State.FAIL)) {
					try {
						logger.debug("Rollback the transaction");
						conn.rollback();
					} catch (SQLException e) {
						logger.error(e.getMessage());
						logger.trace(e.getMessage(), e);
					}
				}
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
					logger.trace(e.getMessage(), e);
				}
				
				logger.debug("Transaction txnId[" + txnId + "] complete");
				
			}
			
		});
		
	}
}
