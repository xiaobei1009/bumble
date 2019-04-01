package org.bumble.core.remoting.client;

public interface IRemotingTransporterClient {
	/**
	 * Indicate if this transporter is failed to connect to server or not
	 * 
	 * @return
	 */
	Boolean isFailedToConnect();
	
	/**
	 * Failed to connect
	 */
	void failedToConnect();
	
	/**
	 * Indicate if this transporter is closed or not
	 * 
	 * @return
	 */
	Boolean isClosed();
	
	/**
	 * Indicate if this transporter is connecting
	 * 
	 * @return
	 */
	Boolean isConnecting();
	
	/**
	 * Indicate if this transporter connection to its server lost
	 * 
	 * @return
	 */
	Boolean isConnectionLost();
	
	/**
	 * Mark this transporter lost connection
	 */
	void lostConnection();
	
	/**
	 * Indicate if this transporter is successfully connected to server or not
	 * 
	 * @return
	 */
	Boolean isConnected();
	
	/**
	 * Get last available time stamp, which is identified by heart beat health check 
	 * 
	 * @return
	 */
	Long getLastAvailableTimestamp();
	
	/**
	 * Update last available time stamp, which is processed by heart beat health check
	 * 
	 * @param lastAvailableTimestamp
	 */
	void setLastAvailableTimestamp(Long lastAvailableTimestamp);
	
	/**
	 * Close this transporter
	 * 
	 */
	void close();
	
	/**
	 * Start to connect to an end point
	 *
	 * @throws Exception
	 */
	void start() throws Exception;
	
	/**
	 * Restart to connect to an end point
	 * 
	 * @throws Exception
	 */
	void restart() throws Exception;
	
	/**
	 * Get the name
	 * 
	 * @return
	 */
	String getName();
	
	/**
	 * Send message
	 * 
	 * @param msg
	 */
	void sendMsg(String msg);
	
	/**
	 * Enable heart beat or not
	 * 
	 * @param enableHeart
	 */
	void enableHeart(Boolean enableHeart);
}
