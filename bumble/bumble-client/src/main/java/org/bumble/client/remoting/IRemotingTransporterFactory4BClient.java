package org.bumble.client.remoting;

/**
 * Remoting transporter factory for bumble client
 * <p>
 * @author shenxiangyu
 *
 */
public interface IRemotingTransporterFactory4BClient {
	/**
	 * Connect to a Bumble Manager
	 * <p>
	 */
	public void connectToManager();
	
	/**
	 * Indicate if the client is connected to a bumble manager
	 * <p>
	 * @return
	 */
	public Boolean isConnected();
	
	/**
	 * Get connected bumble manager unique name
	 * @return
	 */
	public String getManagerUniqName();
}
