package org.bumble.core.remoting;

import org.bumble.core.remoting.client.IRemotingTransporterClient;
import org.bumble.registry.data.RegistryData;

public interface IRemotingTransporterFactory {
	/**
	 * Start the transporter factory
	 * @throws Exception
	 */
	public void start() throws Exception;
	
	/**
	 * Restart the transporter factory
	 * @throws Exception
	 */
	public void restart() throws Exception;
	
	/**
	 * Close the transporter factory
	 */
	public void close();
	
	/**
	 * Close a transporter client
	 * 
	 * @param client
	 */
	public void closeTransporterClient(IRemotingTransporterClient client);
	
	/**
	 * Do something after registry changed
	 */
	public void doAfterRegistryChange(RegistryData registryData);
	
	/**
	 * Get unique name
	 * @return
	 */
	public String getUniqName();
	
}
