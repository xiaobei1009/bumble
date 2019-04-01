package org.bumble.core.remoting;

import org.bumble.core.remoting.client.IRemotingTransporterClient;
import org.bumble.registry.data.RegistryData;

public interface IRemotingTransporterFactory {
	/**
	 * Start the transporter factory
	 * @throws Exception
	 */
	void start() throws Exception;
	
	/**
	 * Restart the transporter factory
	 * @throws Exception
	 */
	void restart() throws Exception;
	
	/**
	 * Close the transporter factory
	 */
	void close();
	
	/**
	 * Close a transporter client
	 * 
	 * @param client
	 */
	void closeTransporterClient(IRemotingTransporterClient client);
	
	/**
	 * Do something after registry changed
	 */
	void doAfterRegistryChange(RegistryData registryData);
	
	/**
	 * Get unique name
	 * @return
	 */
	String getUniqName();
	
}
