package org.bumble.core.remoting.client;

public interface IRemotingTransporterClient {
	/**
	 * Indicate if this transporter is failed to connect to server or not
	 * 
	 * @return
	 */
	public Boolean isFailedToConnect();
	
	/**
	 * Failed to connect
	 */
	public void failedToConnect();
	
	/**
	 * Indicate if this transporter is closed or not
	 * 
	 * @return
	 */
	public Boolean isClosed();
	
	/**
	 * Indicate if this transporter is connecting
	 * 
	 * @return
	 */
	public Boolean isConnecting();
	
	/**
	 * Indicate if this transporter connection to its server lost
	 * 
	 * @return
	 */
	public Boolean isConnectionLost();
	
	/**
	 * Mark this transporter lost connection
	 */
	public void lostConnection();
	
	/**
	 * Indicate if this transporter is successfully connected to server or not
	 * 
	 * @return
	 */
	public Boolean isConnected();
	
	/**
	 * Get last available time stamp, which is identified by heart beat health check 
	 * 
	 * @return
	 */
	public Long getLastAvailableTimestamp();
	
	/**
	 * Update last available time stamp, which is processed by heart beat health check
	 * 
	 * @param lastAvailableTimestamp
	 */
	public void setLastAvailableTimestamp(Long lastAvailableTimestamp);
	
	/**
	 * Close this transporter
	 * 
	 */
	public void close();
	
	/**
	 * Start to connect to an end point
	 * 
	 * @param ip
	 * @param port
	 * @throws Exception
	 */
	public void start() throws Exception;
	
	/**
	 * Restart to connect to an end point
	 * 
	 * @throws Exception
	 */
	public void restart() throws Exception;
	
	/**
	 * Get the name
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * Send message
	 * 
	 * @param msg
	 */
	public void sendMsg(String msg);
	
	/**
	 * Enable heart beat or not
	 * 
	 * @param enableHeart
	 */
	public void enableHeart(Boolean enableHeart);
}
