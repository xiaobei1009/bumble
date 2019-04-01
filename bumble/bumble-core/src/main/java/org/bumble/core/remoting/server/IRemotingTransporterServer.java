package org.bumble.core.remoting.server;

import java.nio.channels.SocketChannel;
import java.util.Map;

import org.bumble.base.model.Node;

public interface IRemotingTransporterServer {
	
	/**
	 * Start the server
	 * 
	 * @throws Exception
	 */
	void start() throws Exception;
	
	/**
	 * Stop the server
	 * 
	 */
	void close();
	
	/**
	 * Update the last available time stamp for connected bumble clients
	 * 
	 * @param url
	 */
	void updateLastAvailableTimestamp4ClientConnected(String url);
	
	/**
	 * Get connected bumble clients to server
	 * 
	 * @return
	 */
	Map<String, Node> getClientsConnectedToServer();
	
	/**
	 * Update the channel for a connected client
	 * 
	 * @param clientName
	 * @param channel
	 */
	void updateClientChannel(String clientName, SocketChannel channel);
	
	/**
	 * Send message to client
	 * 
	 * @param clientName
	 * @param msg
	 */
	void sendMsgToClient(String clientName, String msg);
}
